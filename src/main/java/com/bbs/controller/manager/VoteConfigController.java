package com.bbs.controller.manager;

import com.alibaba.fastjson.JSON;
import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.VoteConfig;
import com.bbs.service.IVoteConfigService;
import com.bbs.util.DateUtil;
import com.bbs.util.TextUtils;
import com.bbs.util.page.Page;
import com.bbs.util.page.PageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2016/9/29 22:06.
 */
@Controller
@RequestMapping(value = "/config")
public class VoteConfigController extends BaseController {

    @Resource
    private IVoteConfigService voteConfigService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView mav = new ModelAndView("config/list");
        Page<VoteConfig> pages = voteConfigService.findPageConfigs(null, page, size);
        mav.addObject("page", PageHelper.getPageModel(request, pages));
        mav.addObject("voteConfigs", pages.getItems());
        return mav;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ModelAndView find(String title){
        ModelAndView mav = new ModelAndView("/config/list");
        Page<VoteConfig> pages = voteConfigService.findPageConfigs(title, page, size);
        mav.addObject("page", PageHelper.getPageModel(request, pages));
        mav.addObject("voteConfigs", pages.getItems());
        mav.addObject("title", title);
        return mav;
    }

    @RequestMapping(value = "/pub")
    public ModelAndView pub(){
        ModelAndView mav = new ModelAndView("/config/add");
        String day = TextUtils.getConfig("default_vote_day", this);
        Date date = DateUtil.getDateBefore(new Date(), Integer.parseInt(day));
        List<Post> votePosts = postService.findVotePosts(date);
        mav.addObject("votePosts", JSON.toJSON(votePosts));
        return mav;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addOrUpdate(VoteConfig config){
        VoteConfig voteConfig = voteConfigService.getVoteConfigByPost(config.getPostId());
        if(voteConfig != null){
            config.setId(voteConfig.getId());
        }
        voteConfigService.addOrUpdateConfig(config);
        return "redirect:/config/find";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable(value = "id") Integer id){
        ModelAndView mav = new ModelAndView("/config/add");
        mav.addObject("config", voteConfigService.getVoteConfig(id));
        String day = TextUtils.getConfig("default_vote_day", this);
        Date date = DateUtil.getDateBefore(new Date(), Integer.parseInt(day));
        List<Post> votePosts = postService.findVotePosts(date);
        mav.addObject("votePosts", JSON.toJSON(votePosts));
        return mav;
    }
}
