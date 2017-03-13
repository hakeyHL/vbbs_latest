package com.bbs.controller.api;

import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.*;
import com.bbs.service.IEggService;
import com.bbs.service.IVoteConfigService;
import com.bbs.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lihongde on 2016/9/13 10:49
 */
@Controller
@RequestMapping(value = "/egg")
public class EggApiController extends BaseController {

    @Resource
    private IEggService eggService;

    @Resource
    private IVoteConfigService voteConfigService;

    @RequestMapping(value = "/info")
    public ModelAndView eggInfo(Integer postId){
        ModelAndView mav = new ModelAndView("forward:/microForum/egg.jsp");
        mav.addObject("postId", postId);
        return mav;
    }

    /**
     * 砸金蛋,返回结果
     * @return
     */
    @RequestMapping(value = "/brokeEgg", method = RequestMethod.GET)
    @ResponseBody
    public ApiJsonResult brokeEgg(Integer postId) throws  Exception{
        Map<String, Object> result = new HashMap<String, Object>();

        User user = (User)session.getAttribute("existUser");
        //判断当前用户当天是否已经砸过
        VoteConfig config = voteConfigService.getVoteConfigByPost(postId);
        List<AwardsRecords> awardsRecords = new ArrayList<AwardsRecords>();
        if(config.getVoteFreq() == 1){
            awardsRecords = eggService.getRecordByUser(user.getId(), postId, 0);
            if(awardsRecords.size() > 0){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "您今天已经砸过,不能重复砸");
            }
        }else{
            Vote vote = voteService.findVoteByPostId(config.getPostId());
            int  voteTimes =  voteService.countUserVote(user.getId(), vote.getId(), DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
            List<AwardsRecords> userRecords =  eggService.getRecordByUser(user.getId(), vote.getPostId(), config.getVoteTimes());
            if(voteTimes <= userRecords.size()){  //未投票就砸金蛋
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "投票之后才能砸金蛋");
            }
            awardsRecords = eggService.getRecordByUser(user.getId(), postId, config.getVoteTimes());
            if(awardsRecords.size() > config.getVoteTimes()){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "您今天不能再砸金蛋了");
            }
        }



        String message = null;

        List<Awards> prizes = eggService.findAwards();

        logger.debug("-------开始抽奖----");

        int prize = Lottery.lottery(prizes);
        if(prize == 0){
            message = "很遗憾，什么都没有";
        }else{
            String prizeName = this.sendAwardResult(prize);
            if(StringUtils.isNotEmpty(prizeName)){
                message = "恭喜您，获得" + prizeName;
            }else{
                message = "很遗憾，什么都没有";
            }
        }

        logger.debug("抽奖成功----prze=" + message);

        result.put("prize", message);

        AwardsRecords records = new AwardsRecords();
        records.setAwardId(prize);
        records.setPostId(postId);
        records.setUserId(user.getId());
        records.setAwardsContent(message);
        records.setCreateTime(new Date());
        eggService.addAwardsRecord(records);

        return new ApiJsonResult(Constants.JSON_RESULT.OK, result);
    }


    /**
     * 发送抽奖结果到远程服务器
     * @param awardId
     * @throws Exception
     * return result 奖品名称
     */
    private String sendAwardResult(int awardId) throws Exception{
        String awardName = "";
        Awards awards = eggService.getAwards(awardId);
        Map<String, String> mapData = buildMap(awards);
        String message =  createSignMessageString(mapData);
        message = message + "&key=" + TextUtils.getConfig("key", this);
        message = DigestUtils.md5Hex(message).toUpperCase();
        String xml = this.buildRequestData(mapData, message);

        String result = HttpRequestUtil.httpRequest(TextUtils.getConfig("request_url", this), xml);

        Map<String, Object> resultMap =  XmlUtil.xml2map(result);
        logger.debug("返回数据" + resultMap);
        if(resultMap.get("return_code").toString().equals("SUCCESS")){
            awardName = resultMap.get("type_name").toString();
             int num = Integer.parseInt(resultMap.get("balance").toString());
             awards.setAwardsNum(num);
             awards.setAwardsName(awardName);
             eggService.addOrUpdate(awards);
         }

        return awardName;
    }

    private Map<String, String> buildMap(Awards awards){
        Map<String, String> map = new HashMap<String, String>();
        map.put("request_client_id", TextUtils.getConfig("request_client_id", this));
        map.put("nonce_str", HttpRequestUtil.byte2hex(HttpRequestUtil.getRandomBytes(16)));
        map.put("action", TextUtils.getConfig("action", this));
        map.put("createtime", String.valueOf(new Date().getTime() / 1000));
        User user = (User)session.getAttribute("existUser");
        map.put("openid", user.getOpenid());
        map.put("card_type", awards.getAwardsType());
        map.put("quantity", "1");
        return map;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createSignMessageString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 要发送的xml字符串
     * @param mapData
     * @param sign
     * @return
     */
    private String buildRequestData(Map<String, String> mapData, String sign){
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<request_client_id>").append(mapData.get("request_client_id")).append("</request_client_id>");
        sb.append("<nonce_str>").append(mapData.get("nonce_str")).append("</nonce_str>");
        sb.append("<sign>").append(sign).append("</sign>");
        sb.append("<action>").append(mapData.get("action")).append("</action>");
        sb.append("<createtime>").append(mapData.get("createtime")).append("</createtime>");
        sb.append("<openid>").append(mapData.get("openid")).append("</openid>");
        sb.append("<card_type>").append(mapData.get("card_type")).append("</card_type>");
        sb.append("<quantity>").append(mapData.get("quantity")).append("</quantity>");
        sb.append("</xml>");
        return sb.toString();
    }


}
