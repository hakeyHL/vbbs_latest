package com.bbs.service;

import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.PostComment;
import com.bbs.mybatis.model.PostImage;
import com.bbs.util.Constants;
import com.bbs.util.page.Page;

import java.util.Date;
import java.util.List;

/**
 * Created on 2016/9/8.
 */
public interface IPostService {

    /**
     * 根据主键找post
     * @param id
     * @return
     */
    Post findPostById(Integer id);

    /**
     * 根据版块查找帖子
     * @param section
     * @param title
     * @param page
     * @param size
     * @return
     */
    Page<Post> findPagePosts(String section, String title, int page, int size);

    /**
     * 添加或修改帖子
     * @param post
     */
    void addOrUpdate(Post post);


    /**
     * 删除一篇帖子
     * @param postId
     */
    void delete(Integer postId);

    /**
     * 获得所有投票帖
     * @return
     */
    List<Post> findVotePosts();

    /**
     * 获得指定日期之后发布的投票帖
     * @return
     */
    List<Post> findVotePosts(Date date);

    void addPostImage(PostImage postImage);

    List<PostImage> findPostImg(Integer postId);

    List<Post> likeContent(String content);

    void deleteComment(Integer posiTd);

    void deleteImage(Integer postId);

    void deleteThumb(Integer postId);

    int countPostThumb(Integer postId);

    List<Post> findPostsBySection(Integer sectionId);
}
