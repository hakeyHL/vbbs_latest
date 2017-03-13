package com.bbs.controller.manager;

import com.alibaba.fastjson.JSON;
import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.PostImage;
import com.bbs.mybatis.model.Vote;
import com.bbs.mybatis.model.VoteConfig;
import com.bbs.service.*;
import com.bbs.util.ApiJsonResult;
import com.bbs.util.Constants;
import com.bbs.util.FileUtils;
import com.bbs.util.TextUtils;
import com.bbs.util.page.Page;
import com.bbs.util.page.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2016/9/8 16:29
 */
@Controller
@RequestMapping(value = "/post")
public class PostController extends BaseController {

    @Resource
    private IPostService postService;

    @Resource
    private ISectionService sectionService;

    @Resource
    private IUserService userService;

    @Resource
    private IEggService eggService;

    @Resource
    private IVoteConfigService voteConfigService;

    @Resource
    private IPostCommentService postCommentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listPosts() {
        return "redirect:/post/find";
    }


    /**
     * 条件查询帖子列表
     *
     * @param section
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ModelAndView findPosts(String section, String title) {
        ModelAndView mav = new ModelAndView("/post/list");
        Page<Post> pages = postService.findPagePosts(section, title, page, size);
        mav.addObject("page", PageHelper.getPageModel(request, pages));
        List<Post> postList = new ArrayList<Post>();
        for (Post post : pages.getItems()) {
            post.setUser(userService.findUserById(post.getUserId()).getNickName());
            post.setSection(sectionService.getSection(post.getSectionId()).getName());

            //评论数
            int comments = postCommentService.countPostComments(post.getId());
            post.setCommentNum(comments);
            //点赞数
            int thumbNum = postService.countPostThumb(post.getId());
            post.setThumbNum(thumbNum);
            postList.add(post);
        }
        mav.addObject("section", section);
        mav.addObject("title", title);
        mav.addObject("posts", postList);
        return mav;
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish() {
        ModelAndView mav = new ModelAndView("post/add");
        mav.addObject("sections", JSON.toJSON(sectionService.getAllSection()));
        return mav;
    }

    /**
     * 发帖或修改帖子
     *
     * @param post
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addOrUpdate(Post post, String files) {
        post.setPublishTime(new Date());

        if(post.getBrowseNum() == null){
            post.setBrowseNum(0);
        }
        if(post.getCommentNum() == null){
            post.setCommentNum(0);
        }
        if(post.getThumbNum() == null){
            post.setThumbNum(0);
        }

        if (post.getStatus() == null) {
            post.setStatus(Constants.POST_STATUS.COMMON);
        }
        if(post.getIsApprove() == null){
            post.setIsApprove(Constants.YES_OR_NO.NO);
        }
        post.setUserId(Constants.ADMIN_USER_ID);
        post.setIsDelete(Constants.YES_OR_NO.NO);
        if (post.getTop() == null) {
            post.setTop(Constants.YES_OR_NO.NO);
        }

        post.setContentCopy(post.getContent());
        String content = post.getContent();
        List<String> imgList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(content)) {
            imgList = TextUtils.getImgSrc(content);
            post.setContent(TextUtils.delHTMLTag(content));
        }

        postService.addOrUpdate(post);

        for (String imgUrl : imgList) {
            PostImage pm = new PostImage();
            pm.setPostId(post.getId());
            pm.setImgUrl(imgUrl);
            postService.addPostImage(pm);
        }


        return "redirect:/post/find";
    }


    /**
     * 帖子详情
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable(value = "id") Integer postId) {
        ModelAndView mav = new ModelAndView("post/add");
        mav.addObject("post", postService.findPostById(postId));
        mav.addObject("sections", JSON.toJSON(sectionService.getAllSection()));
        return mav;
    }

    /**
     * 帖子置顶
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "/top/{id}", method = RequestMethod.GET)
    public String top(@PathVariable(value = "id") Integer postId) {
        Post post = postService.findPostById(postId);
        if (post.getTop() == Constants.YES_OR_NO.YES) {
            post.setTop(Constants.YES_OR_NO.NO);
        } else {
            post.setTop(Constants.YES_OR_NO.YES);
        }
        postService.addOrUpdate(post);
        return "redirect:/post/find";
    }

    /**
     * 获得所有版块
     *
     * @return
     */
    @RequestMapping(value = "getSections", method = RequestMethod.GET)
    @ResponseBody
    public ApiJsonResult getSections() {
        return new ApiJsonResult(Constants.JSON_RESULT.OK, sectionService.getAllSection());
    }

    /**
     * 删除一篇帖子并且删除跟该帖相关信息, 只提供给管理后台
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiJsonResult deletePost(@PathVariable(value = "id") Integer postId) {
        //删除帖子
        postService.delete(postId);
        //删除帖子评论
        postService.deleteComment(postId);
        //删除图片
        postService.deleteImage(postId);
        //删除历史点赞

        postService.deleteThumb(postId);
        //删除历史投票以及候选人
        Vote vote = voteService.findVoteByPostId(postId);
        if(vote != null){
            voteService.deleteByPost(postId);
            voteService.deleteVoteCandidate(vote.getId());
            voteService.deleteUserVote(vote.getId());
        }

        //删除中奖纪录
        eggService.deleteByPost(postId);

        //删除投票配置
        voteConfigService.deleteByPost(postId);

        return new ApiJsonResult(Constants.JSON_RESULT.OK);
    }

    /**
     * 审核， 如果已审核则改为未审核  否则改为审核
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    @ResponseBody
    public ApiJsonResult approve(@RequestParam(value = "id") Integer postId) {
        Post post = postService.findPostById(postId);
        if (post.getIsApprove() == Constants.YES_OR_NO.YES) {
            post.setIsApprove(Constants.YES_OR_NO.NO);
        } else {
            Vote vote = voteService.findVoteByPostId(postId);
            if(post.getType() == Constants.POST_TYPE.VOTE_POST && vote == null){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "请先发起投票");
            }

            VoteConfig config = voteConfigService.getVoteConfigByPost(postId);
            if(post.getType() == Constants.POST_TYPE.VOTE_POST && config == null){
                return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "请先配置投票贴");
            }
            post.setIsApprove(Constants.YES_OR_NO.YES);
        }

        postService.addOrUpdate(post);
            return new ApiJsonResult(Constants.JSON_RESULT.OK);
    }

    @RequestMapping(value = "/ckeditor/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartFile upload) {
        response.setCharacterEncoding("GBK");
        PrintWriter out;
        String callback = request.getParameter("CKEditorFuncNum");
        try {
            if (!upload.isEmpty()) {
                String coverName = FileUtils.generatePictureName(upload);
                String coverPath = TextUtils.getConfig("post_image", this);
                FileUtils.transferFile(coverPath, coverName, upload);
                out = response.getWriter();
                response.reset();
                out.println("<script type=\"text/javascript\">");
                out.println("window.parent.CKEDITOR.tools.callFunction("
                        + callback + ",'" + basePath + TextUtils.getConfig("user_avatar_url", this) + coverName + "',''" + ")"); // 相对路径用于显示图片
                out.println("</script>");
                out.flush();
                out.close();
                return "upload Success";
            } else {
                return "upload Failed";
            }

        } catch (IOException e) {
            return "upload Failed";
        }
    }

}
