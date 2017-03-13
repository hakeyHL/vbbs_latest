package com.bbs.controller.api.vo;

import java.io.Serializable;

/**
 * jsapi_ticket
 * @author Enhon
 *
 */
public class ReturnJsapiticketVo implements Serializable{
	private static final long serialVersionUID = -1750138445768408860L;
	private String request_client_id;
	private String nonce_str;
	private String sign;
	private String return_code;
	private String value;
	public String getRequest_client_id() {
		return request_client_id;
	}
	public void setRequest_client_id(String request_client_id) {
		this.request_client_id = request_client_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
