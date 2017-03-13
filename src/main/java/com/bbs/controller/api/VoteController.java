package com.bbs.controller.api;

import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.*;
import com.bbs.service.IVoteConfigService;
import com.bbs.util.ApiJsonResult;
import com.bbs.util.Constants;
import com.bbs.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by HL&WN on 2016/9/10.
 */
@Controller
@RequestMapping("/vote")
public class VoteController extends BaseController{

    @Resource
    private IVoteConfigService voteConfigService;

    /**
     * 返回所有的投票帖子
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public List<Post> getVotePostList() {

        List<Post> votePostList = postService.findVotePosts();

        return votePostList;
    }

    /**
     * 提交投票
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ApiJsonResult vote(Integer voteId, Integer postId,  @RequestParam(value = "names[]") String[] names){

        User user = (User) session.getAttribute("existUser");

        Long timestamp = (Long)session.getAttribute(user.getId().toString());
        if(timestamp == null){
            session.setAttribute(user.getId().toString(), new Date().getTime());
        }else{
            long now = new Date().getTime();
            if(now - timestamp < 10 * 1000){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "访问太频繁，请稍后重试");
            }
        }

        session.setAttribute(user.getId().toString(), new Date().getTime());

        synchronized(this) {
            //候选人去重, 防止多次重复提交
            Set<String> prodCodeSet = new HashSet<String>();
            prodCodeSet.addAll(Arrays.asList(names));
            names = prodCodeSet.toArray(new String[]{});

            VoteConfig config = voteConfigService.getVoteConfigByPost(postId);
            List<UserVote> userVotes = new ArrayList<UserVote>();

            if (config.getVoteFreq() == 1) {
                userVotes = voteService.findUserVote(user.getId(), voteId, 0);
                if (userVotes.size() > 0) {
                    return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "您已投过票，不能再投");
                }
            } else {
                int count = voteService.countUserVote(user.getId(), voteId, DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                logger.debug("you have vote " + count + " times!");
                if (count >= config.getVoteTimes()) {
                    return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "您今天不能再投票了");
                }
            }

            Date voteTime = new Date();

            for (String name : names) {
                logger.debug("------------提交投票" + "voteId=" + voteId + " name=" + name.trim());

                UserVote userVote = new UserVote();
                userVote.setUserId(user.getId());
                userVote.setVoteId(voteId);
                userVote.setName(name.trim());
                userVote.setVoteTime(voteTime);

                //查询是否有相同记录，如果没有则插入
                List<UserVote> list = voteService.find(user.getId(), voteId, name.trim(), voteTime);
                if (list.size() == 0) {
                    voteService.addUserVote(userVote);
                }
            }

            logger.debug("-------------------投票成功");

            return new ApiJsonResult(Constants.JSON_RESULT.OK);
        }
    }
}
