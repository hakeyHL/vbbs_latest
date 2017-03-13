package com.bbs.service.impl;

import com.bbs.mybatis.inter.PostCommentMapper;
import com.bbs.mybatis.inter.PostImageMapper;
import com.bbs.mybatis.inter.PostMapper;
import com.bbs.mybatis.inter.UserThumbPostMapper;
import com.bbs.mybatis.model.*;
import com.bbs.service.IPostService;
import com.bbs.util.Constants;
import com.bbs.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
@Service
@Transactional
public class PostServiceImpl implements IPostService {

    @Resource
    PostMapper postMapper;
    @Resource
    PostCommentMapper postCommentMapper;

    @Resource
    private PostImageMapper postImageMapper;

    @Resource
    private UserThumbPostMapper userThumbPostMapper;

    @Override
    public Post findPostById(Integer id) {
        return postMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Post> findPagePosts(String sectionId, String title, int page, int size) {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(Constants.YES_OR_NO.NO);
        example.setOrderByClause("publish_time desc");
        RowBounds rowBounds = new RowBounds((page - 1) * size, size);
        List<Post> list = new ArrayList<Post>();
        if(StringUtils.isNotEmpty(sectionId) || StringUtils.isNotEmpty(title)){
            list = postMapper.selectByKeywordsWithBLOBsWithRowbounds(sectionId, title, rowBounds);
            return new Page<Post>(page, size, list.size(), list);
        }else{
            list = postMapper.selectByExampleWithBLOBsWithRowbounds(example, rowBounds);
            return new Page<Post>(page, size, postMapper.countByExample(example), list);
        }
    }

    @Override
    public void addOrUpdate(Post post) {
        Integer id = post.getId();
        if (id == null) {
            postMapper.insertSelective(post);
        } else {
            postMapper.updateByPrimaryKeyWithBLOBs(post);
        }
    }

    @Override
    public void delete(Integer postId) {
        postMapper.deleteByPrimaryKey(postId);
    }

    @Override
    public List<Post> findVotePosts() {
        PostExample example = new PostExample();

        PostExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(Constants.POST_TYPE.VOTE_POST);
        criteria.andIsDeleteEqualTo(Constants.YES_OR_NO.NO);
        criteria.andIsApproveEqualTo(Constants.YES_OR_NO.YES);
        //设置排序字段
        example.setOrderByClause("publish_time");

        return postMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Post> findVotePosts(Date date) {
        PostExample example = new PostExample();

        PostExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(Constants.POST_TYPE.VOTE_POST);
        criteria.andIsDeleteEqualTo(Constants.YES_OR_NO.NO);
        criteria.andIsApproveEqualTo(Constants.YES_OR_NO.NO);
        criteria.andPublishTimeGreaterThan(date);
        //设置排序字段
        example.setOrderByClause("publish_time");

        return postMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public void addPostImage(PostImage postImage) {
        postImageMapper.insert(postImage);
    }

    @Override
    public List<PostImage> findPostImg(Integer postId) {
        PostImageExample example = new PostImageExample();
        PostImageExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        return postImageMapper.selectByExample(example);
    }

    @Override
    public List<Post> likeContent(String content) {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andContentLike("%" + content + "%");

        return postMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public void deleteComment(Integer postId) {
        PostCommentExample example = new PostCommentExample();
        PostCommentExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(postCommentMapper.countByExample(example) > 0){
            postCommentMapper.deleteByExample(example);
        }

    }

    @Override
    public void deleteImage(Integer postId) {
        PostImageExample exapmple = new PostImageExample();
        PostImageExample.Criteria criteria = exapmple.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(postImageMapper.countByExample(exapmple) > 0){
            postImageMapper.deleteByExample(exapmple);
        }

    }

    @Override
    public void deleteThumb(Integer postId) {
        UserThumbPostExample example = new UserThumbPostExample();
        UserThumbPostExample.Criteria criteria = example.createCriteria();
        criteria.andPostidEqualTo(postId);
        if(userThumbPostMapper.countByExample(example) > 0){
            userThumbPostMapper.deleteByExample(example);
        }

    }

    @Override
    public int countPostThumb(Integer postId) {
        UserThumbPostExample example = new UserThumbPostExample();
        UserThumbPostExample.Criteria criteria = example.createCriteria();
        criteria.andPostidEqualTo(postId);
        return userThumbPostMapper.countByExample(example);
    }

    @Override
    public List<Post> findPostsBySection(Integer sectionId) {
        PostExample example = new PostExample();
        PostExample.Criteria criteria = example.createCriteria();
        criteria.andSectionIdEqualTo(sectionId);
        return postMapper.selectByExampleWithBLOBs(example);
    }


}
