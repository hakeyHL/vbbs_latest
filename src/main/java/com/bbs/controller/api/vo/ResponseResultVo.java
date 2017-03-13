package com.bbs.controller.api.vo;

import java.util.List;

/**
 * Created by hs on 2016年09月13日.
 * Time 15:26
 */
public class ResponseResultVo {

    private String errorCode = "10000";
    private String msg = "ok";
    private int thumbNumber;
    private List dataList;
    private int nextPage;
    private Long commentNum;

    public Long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Long commentNum) {
        this.commentNum = commentNum;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getThumbNumber() {
        return thumbNumber;
    }

    public void setThumbNumber(int thumbNumber) {
        this.thumbNumber = thumbNumber;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
