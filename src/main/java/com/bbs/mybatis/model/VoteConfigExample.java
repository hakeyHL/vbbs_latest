package com.bbs.mybatis.model;

import java.util.ArrayList;
import java.util.List;

public class VoteConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VoteConfigExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPostIdIsNull() {
            addCriterion("`post_id` is null");
            return (Criteria) this;
        }

        public Criteria andPostIdIsNotNull() {
            addCriterion("`post_id` is not null");
            return (Criteria) this;
        }

        public Criteria andPostIdEqualTo(Integer value) {
            addCriterion("`post_id` =", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotEqualTo(Integer value) {
            addCriterion("`post_id` <>", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdGreaterThan(Integer value) {
            addCriterion("`post_id` >", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`post_id` >=", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdLessThan(Integer value) {
            addCriterion("`post_id` <", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdLessThanOrEqualTo(Integer value) {
            addCriterion("`post_id` <=", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdIn(List<Integer> values) {
            addCriterion("`post_id` in", values, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotIn(List<Integer> values) {
            addCriterion("`post_id` not in", values, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdBetween(Integer value1, Integer value2) {
            addCriterion("`post_id` between", value1, value2, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`post_id` not between", value1, value2, "postId");
            return (Criteria) this;
        }

        public Criteria andVoteFreqIsNull() {
            addCriterion("`vote_freq` is null");
            return (Criteria) this;
        }

        public Criteria andVoteFreqIsNotNull() {
            addCriterion("`vote_freq` is not null");
            return (Criteria) this;
        }

        public Criteria andVoteFreqEqualTo(Integer value) {
            addCriterion("`vote_freq` =", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqNotEqualTo(Integer value) {
            addCriterion("`vote_freq` <>", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqGreaterThan(Integer value) {
            addCriterion("`vote_freq` >", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqGreaterThanOrEqualTo(Integer value) {
            addCriterion("`vote_freq` >=", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqLessThan(Integer value) {
            addCriterion("`vote_freq` <", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqLessThanOrEqualTo(Integer value) {
            addCriterion("`vote_freq` <=", value, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqIn(List<Integer> values) {
            addCriterion("`vote_freq` in", values, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqNotIn(List<Integer> values) {
            addCriterion("`vote_freq` not in", values, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqBetween(Integer value1, Integer value2) {
            addCriterion("`vote_freq` between", value1, value2, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteFreqNotBetween(Integer value1, Integer value2) {
            addCriterion("`vote_freq` not between", value1, value2, "voteFreq");
            return (Criteria) this;
        }

        public Criteria andVoteTimesIsNull() {
            addCriterion("`vote_times` is null");
            return (Criteria) this;
        }

        public Criteria andVoteTimesIsNotNull() {
            addCriterion("`vote_times` is not null");
            return (Criteria) this;
        }

        public Criteria andVoteTimesEqualTo(Integer value) {
            addCriterion("`vote_times` =", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesNotEqualTo(Integer value) {
            addCriterion("`vote_times` <>", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesGreaterThan(Integer value) {
            addCriterion("`vote_times` >", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("`vote_times` >=", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesLessThan(Integer value) {
            addCriterion("`vote_times` <", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesLessThanOrEqualTo(Integer value) {
            addCriterion("`vote_times` <=", value, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesIn(List<Integer> values) {
            addCriterion("`vote_times` in", values, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesNotIn(List<Integer> values) {
            addCriterion("`vote_times` not in", values, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesBetween(Integer value1, Integer value2) {
            addCriterion("`vote_times` between", value1, value2, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andVoteTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("`vote_times` not between", value1, value2, "voteTimes");
            return (Criteria) this;
        }

        public Criteria andMaxVoteIsNull() {
            addCriterion("`max_vote` is null");
            return (Criteria) this;
        }

        public Criteria andMaxVoteIsNotNull() {
            addCriterion("`max_vote` is not null");
            return (Criteria) this;
        }

        public Criteria andMaxVoteEqualTo(Integer value) {
            addCriterion("`max_vote` =", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteNotEqualTo(Integer value) {
            addCriterion("`max_vote` <>", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteGreaterThan(Integer value) {
            addCriterion("`max_vote` >", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteGreaterThanOrEqualTo(Integer value) {
            addCriterion("`max_vote` >=", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteLessThan(Integer value) {
            addCriterion("`max_vote` <", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteLessThanOrEqualTo(Integer value) {
            addCriterion("`max_vote` <=", value, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteIn(List<Integer> values) {
            addCriterion("`max_vote` in", values, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteNotIn(List<Integer> values) {
            addCriterion("`max_vote` not in", values, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteBetween(Integer value1, Integer value2) {
            addCriterion("`max_vote` between", value1, value2, "maxVote");
            return (Criteria) this;
        }

        public Criteria andMaxVoteNotBetween(Integer value1, Integer value2) {
            addCriterion("`max_vote` not between", value1, value2, "maxVote");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}