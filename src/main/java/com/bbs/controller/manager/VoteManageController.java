package com.bbs.controller.manager;

import com.alibaba.fastjson.JSON;
import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.Vote;
import com.bbs.mybatis.model.VoteCandidate;
import com.bbs.service.IPostService;
import com.bbs.service.IVoteService;
import com.bbs.util.ApiJsonResult;
import com.bbs.util.Constants;
import com.bbs.util.DateUtil;
import com.bbs.util.TextUtils;
import com.bbs.util.page.Page;
import com.bbs.util.page.PageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lihongde on 2016/9/12 15:09
 */
@Controller
@RequestMapping(value = "/vote")
public class VoteManageController extends BaseController {

    @Resource
    private IVoteService voteService;

    @Resource
    private IPostService postService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listVotes() {
        return "redirect:/vote/find";
    }

    /**
     * 条件查询投票列表
     * @param section
     * @param title
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ModelAndView findVotes(String section, String title) {
        ModelAndView mav = new ModelAndView("/vote/list");
        Page<Vote> pages = voteService.findPageVotes(section, title, page, size);
        mav.addObject("page", PageHelper.getPageModel(request, pages));
        List<Vote> voteList = new ArrayList<Vote>();
        for (Vote vote : pages.getItems()) {
            vote.setPost(postService.findPostById(vote.getPostId()).getTitle());
            Post post  = postService.findPostById(vote.getPostId());
            if(post.getExpireTime() != null && post.getExpireTime().getTime() < new Date().getTime()){
                vote.setStatus(Constants.VOTE_STATUS.VOTE_FINISH);
            }

            int voteNum = voteService.voteNum(vote.getId(), true);
            vote.setVoteNum(voteNum);
            voteList.add(vote);
        }
        mav.addObject("votes", voteList);
        mav.addObject("section", section);
        mav.addObject("title", title);
        return mav;
    }

    /**
     * 发起投票
     * @return
     */
    @RequestMapping(value = "/pub", method = RequestMethod.GET)
    public ModelAndView candidate(){
        ModelAndView mav = new ModelAndView("vote/add");
        String day = TextUtils.getConfig("default_vote_day", this);
        Date date = DateUtil.getDateBefore(new Date(), Integer.parseInt(day));
        List<Post> votePosts = postService.findVotePosts(date);
        mav.addObject("votePosts", JSON.toJSON(votePosts));
        return mav;
    }

    /**
     * 保存投票以及候选人信息
     * @param vote
     * @param candidateFile 候选人文件
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiJsonResult addVote(Vote vote, MultipartFile candidateFile){
        vote.setStatus(Constants.VOTE_STATUS.VOTE_ING);

        Integer voteId = 0;
        Vote boforeVote = voteService.findVoteByPostId(vote.getPostId());
        if(boforeVote == null){
            voteService.addOrUpdateVote(vote);
            voteId = vote.getId();
        }else{
            voteId = boforeVote.getId();
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = voteService.importExcelFile(candidateFile, voteId);
            result.put("success", true);
        } catch (Exception e) {
            logger.error("导入候选人失败");
            result.put("success", false);
            e.printStackTrace();
            return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, result);
        }

        return new ApiJsonResult(Constants.JSON_RESULT.OK, result);
    }

    /**
     * 停止投票
     * @return
     */
    @RequestMapping(value = "/stop/{id}", method = RequestMethod.GET)
    public String stop(@PathVariable(value = "id") Integer voteId){
        Vote vote = voteService.getVote(voteId);
        Post post = postService.findPostById(vote.getPostId());
        if(post.getType() == Constants.POST_TYPE.VOTE_POST){
            post.setExpireTime(new Date());
            postService.addOrUpdate(post);
        }
        vote.setStatus(Constants.VOTE_STATUS.VOTE_FINISH);
        voteService.addOrUpdateVote(vote);
        return "redirect:/vote/find";
    }

    /**
     * 投票结果统计
     * @param voteId
     * @return
     */
    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public ModelAndView voteResult(@PathVariable(value = "id") Integer voteId){
        ModelAndView mav = new ModelAndView("vote/result");
        Page<VoteCandidate> pages =  voteService.findCandidateByVoteId(voteId, page, size);
        mav.addObject("page", PageHelper.getPageModel(request, pages));

        List<VoteCandidate> list = new ArrayList<VoteCandidate>();
        for(VoteCandidate vc : pages.getItems()){
            Integer count = voteService.countCandidateVote(voteId, vc.getName());
            vc.setVotes(count);
            list.add(vc);
        }

        int voteNum = voteService.voteNum(voteId, false);

        mav.addObject("voteNum", voteNum);
        mav.addObject("candidates", list);
        Vote vote = voteService.getVote(voteId);
        Post post = postService.findPostById(vote.getPostId());
        mav.addObject("postTitle", post.getTitle());
        return mav;
    }
}
