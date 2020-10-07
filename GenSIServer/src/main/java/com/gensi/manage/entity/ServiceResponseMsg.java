package com.gensi.manage.entity;

import java.util.Map;

public class ServiceResponseMsg {

	private ServiceResponseHeader header;
	private Map<String,Object> body;
	
	public ServiceResponseHeader getHeader() {
		return header;
	}
	public void setHeader(ServiceResponseHeader header) {
		this.header = header;
	}
	public Map<String, Object> getBody() {
		return body;
	}
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
}
