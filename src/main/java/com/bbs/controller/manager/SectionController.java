package com.bbs.controller.manager;

import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.Section;
import com.bbs.service.IPostService;
import com.bbs.service.ISectionService;
import com.bbs.util.ApiJsonResult;
import com.bbs.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huli on 2016/9/8 15:42
 * TODO huli&wuning
 * 板块管理
 */
@Controller
@RequestMapping(value = "/section")
public class SectionController extends BaseController {

    @Resource
    private ISectionService sectionService;

    @Resource
    private IPostService postService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView mav = new ModelAndView("section/list");
        mav.addObject("sections", sectionService.getAllSection());
        return mav;
    }

    @RequestMapping(value = "/pub")
    public String pub(){
        return "section/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addOrUpdate(Section section){
        section.setIsDelete(Constants.YES_OR_NO.NO);
        sectionService.addOrUpdate(section);
        return "redirect:/section/list";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable(value = "id") Integer id){
        ModelAndView mav = new ModelAndView("section/add");
        mav.addObject("section", sectionService.getSection(id));
        return mav;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiJsonResult delete(@PathVariable(value = "id") Integer id){

        List<Post> postList = postService.findPostsBySection(id);
        for(Post post : postList){
            if(post.getIsApprove() == Constants.YES_OR_NO.YES){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "不能删除该板块");
            }
        }

        Section section = sectionService.getSection(id);
        section.setIsDelete(Constants.YES_OR_NO.YES);
        sectionService.addOrUpdate(section);
        return new ApiJsonResult(Constants.JSON_RESULT.OK);
    }
}
