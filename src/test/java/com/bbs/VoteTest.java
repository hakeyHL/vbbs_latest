package com.bbs;

import com.bbs.context.Base;
import com.bbs.mybatis.model.UserVote;
import com.bbs.service.IVoteService;
import com.bbs.util.DateUtil;
import org.junit.*;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2016/9/19 10:08
 */
public class VoteTest extends Base {

    @Resource
    private IVoteService voteService;

    @org.junit.Test
    public void userVote(){
        List<UserVote> userVotes = voteService.findUserVote(3, 101, 0);
        for(UserVote userVote : userVotes){
            logger.info("name=" + userVote.getName() + " " + DateUtil.formatDate(userVote.getVoteTime(), "yyyy-MM-dd HH:MM:SS"));
        }
    }

    @Test
    public void countUserVote(){
        int count = voteService.countUserVote(3, 101, "2016-09-21");
        logger.info("voteCount : " + count);
    }
}
