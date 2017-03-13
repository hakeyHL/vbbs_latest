package com.bbs.mybatis.model;

public class VoteConfig {
    private Integer id;

    private Integer postId;

    private Integer voteFreq;

    private Integer voteTimes;

    private Integer maxVote;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getVoteFreq() {
        return voteFreq;
    }

    public void setVoteFreq(Integer voteFreq) {
        this.voteFreq = voteFreq;
    }

    public Integer getVoteTimes() {
        return voteTimes;
    }

    public void setVoteTimes(Integer voteTimes) {
        this.voteTimes = voteTimes;
    }

    public Integer getMaxVote() {
        return maxVote;
    }

    public void setMaxVote(Integer maxVote) {
        this.maxVote = maxVote;
    }
}