package com.bbs.service;

import com.bbs.mybatis.model.UserVote;
import com.bbs.mybatis.model.Vote;
import com.bbs.mybatis.model.VoteCandidate;
import com.bbs.util.page.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongde on 2016/9/12 15:43
 */
public interface IVoteService {

    /**
     * 条件查询投票记录
     * @param section
     * @param title
     * @param page
     * @param size
     * @return
     */
    Page<Vote> findPageVotes(String section, String title, int page, int size);


    /**
     * 发起投票
     * @param vote
     */
    void addOrUpdateVote(Vote vote);

    /**
     * 候选人导入
     * @param candidateFile
     * @param voteId
     * @return
     */
    Map<String, Object> importExcelFile(MultipartFile candidateFile, Integer voteId) throws Exception;

    Vote getVote(Integer voteId);

    /**
     * 获得投票帖的候选人
     * @param voteId
     * @return
     */
    Page<VoteCandidate> findCandidateByVoteId(Integer voteId, int page, int size);

    List<VoteCandidate> getCandidateByVoteId(Integer voteId);

    VoteCandidate getCandidate(Integer voteId, String name);

    void updateCandidate(VoteCandidate candidate);

    /**
     * 添加用户投票记录
     * @param userVote
     */
    void addUserVote(UserVote userVote);

    /**
     * 查询用户投票记录
     * @param userId
     * @param voteId
     * @param times 投票次数 0仅一次  1每天一次 2每天2次以此类推
     * @return
     */
    List<UserVote> findUserVote(Integer userId, Integer voteId, Integer times);

    Vote findVoteByPostId(Integer postId);

    void delete(Integer voteId);

    void deleteByPost(Integer postId);

    void deleteVoteCandidate(Integer voteId);

    void deleteUserVote(Integer voteId);

    Integer countCandidateVote(Integer voteId, String name);

    int countUserVote(Integer userId, Integer voteId, String date);

    List<UserVote> find(Integer userId, Integer voteId, String name, Date voteTime);

    int voteNum(Integer voteId, boolean distinct);
}
