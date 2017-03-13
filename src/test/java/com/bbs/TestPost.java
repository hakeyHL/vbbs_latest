package com.bbs;

import com.bbs.context.Base;
import com.bbs.mybatis.model.Post;
import com.bbs.service.IPostService;
import com.bbs.util.DateUtil;
import com.bbs.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2016/9/9 15:42
 */
public class TestPost extends Base {

    @Resource
    private IPostService postService;

    @Test
    public void list(){
        postService.findPagePosts("", "", 1, 10);
    }

    @Test
    public void findVotePosts(){
        String day = TextUtils.getConfig("default_vote_day", this);
        Date date = DateUtil.getDateBefore(new Date(), Integer.parseInt(day));
        List<Post> list = postService.findVotePosts(date);
        for(Post post : list){
            logger.info("post--- title=" + post.getTitle() + "  type=" + post.getType());
        }
    }

    @Test
    public void contentLike(){
       List<Post> posts =  postService.likeContent("图");
        for(Post post :posts){
            logger.info("post--- title=" + post.getTitle() + "  content=" + post.getContent());
        }
    }
}
