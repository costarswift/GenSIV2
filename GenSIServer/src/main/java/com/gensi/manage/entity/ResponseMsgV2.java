package com.gensi.manage.entity;

/**
 * 同步返回的消息
 */
public class ResponseMsgV2 {
	//返回的错误码
	public static final String CODE_MISSING_PARAM = "00100";
	public static final String MESSAGE_MISSING_PARAM = "接口参数缺失";
	
	public static final String CODE_DECRYPED_ERROR = "00101";
	public static final String MESSAGE_DECRYPED_ERROR = "接口报文解密失败，请检查header参数";
	
	private String code;
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
