package com.bbs.controller.api.vo;

import java.io.Serializable;

/**
 * jsapi_ticket
 * @author Enhon
 *
 */
public class JSSDKSignature implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1038869700457587151L;
	private String noncestr;
	private String signature;
	private String timestamp;
	private String appid;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
