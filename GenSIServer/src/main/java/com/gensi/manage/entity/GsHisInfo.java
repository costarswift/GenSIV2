package com.gensi.manage.entity;

/**
 * GenSI接口响应记录
 * @author roykingw
 * @date 2017年7月25日 -- 下午4:52:53
 */
public class GsHisInfo {

	private String transId;
	
	private String reqBody;
	
	private String rspBody;
	
	private String intime;
	
	private String serviceCode;
	
	private String rsptime;
	
	private String sysId;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getReqBody() {
		return reqBody;
	}

	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}

	public String getRspBody() {
		return rspBody;
	}

	public void setRspBody(String rspBody) {
		this.rspBody = rspBody;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getRsptime() {
		return rsptime;
	}

	public void setRsptime(String rsptime) {
		this.rsptime = rsptime;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
}
