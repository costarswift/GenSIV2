package com.gensi.manage.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 2017年3月6日 蜂易贷接口相互的请求回复。
 * @author ftoul090
 *
 */
public class GsResponse {

	private String transId;
	
	private String result;
	
	private String desc;
	
	private String serviceCode;

	public GsResponse(){
		
	}
	
	public GsResponse(String serviceCode,String transId,String result,String desc){
		this.serviceCode = serviceCode;
		this.transId = transId;
		this.result = result;
		this.desc = desc;
	}
	
	
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public JSONObject toJsonFormat(){
		JSONObject res = new JSONObject();
		
		JSONObject header = new JSONObject();
		header.put("serviceCode", this.getServiceCode());
		
		JSONObject body = new JSONObject();
		body.put("transId", this.getTransId());
		body.put("result", this.getResult());
		body.put("desc", this.getDesc());
		
		res.put("header", header);
		res.put("body", body);
		
		return res;
	}
}
