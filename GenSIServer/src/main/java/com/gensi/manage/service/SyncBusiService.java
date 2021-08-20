package com.gensi.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.entity.RequestMsg;
import com.gensi.manage.entity.ServiceResponseHeader;
import com.gensi.manage.entity.ServiceResponseMsg;
import com.gensi.module.mobileArea.PhoneAreaService;
import com.gensi.module.phoneTag.PhoneTagInfo;
import com.gensi.module.phoneTag.PhoneTagService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 具体处理业务逻辑
 */
@Service
public class SyncBusiService {

	@Resource
	private PhoneTagService phoneTagService;
	@Resource
	private PhoneAreaService phoneAreaService;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public ServiceResponseMsg doProcess(String sysId, String transId, String serviceCode, Map<String, Object> requestMsg) {
		logger.info("start doProcess:sysId => "+sysId+";serviceCode => "+serviceCode+";requestMsg => "+requestMsg);
		// =================================手机号码标注业务==========================================
		if (RequestMsg.MOBILE_MARK_SERVICE.equals(serviceCode)) {
			logger.info("process entry => 进入手机号码标注接口对接");
			 // 转换body为业务对象
			 PhoneTagInfo requestInfo = new PhoneTagInfo();
			 requestInfo.setTransId(transId);
			 requestInfo.setMobile(requestMsg.get("mobile").toString());
			logger.info("process info : 手机号码标注同步推送");
			return phoneTagService.service(requestInfo);
			// 返回正确接收请求的响应信息
			// =================================手机号码归属省==========================================
		}else if(RequestMsg.MOBILE_AREA_SERVICE.equals(serviceCode)){
			logger.info("process entry => 进入手机号码标注接口对接");
			 // 转换body为业务对象
			 PhoneTagInfo requestInfo = new PhoneTagInfo();
			 requestInfo.setTransId(transId);
			 requestInfo.setMobile(requestMsg.get("mobile").toString());
			 // 返回正确接收请求的响应信息
			 logger.info("process info : 手机号码归属同步推送");
			 return phoneAreaService.service(requestInfo);
		}else {
			logger.info("doProcess : entry => 没有匹配的接口，请确认serviceCode是否正确。");
			ServiceResponseMsg serviceResponse = new ServiceResponseMsg();

			ServiceResponseHeader serviceResponseHeader = new ServiceResponseHeader();
			serviceResponseHeader.setTransId(transId);
			serviceResponseHeader.setIntime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			serviceResponseHeader.setServiceCode(serviceCode);
			serviceResponse.setHeader(serviceResponseHeader);

			JSONObject rspBody = new JSONObject();
			rspBody.put("error", "暂不支持的服务");
			serviceResponse.setBody(rspBody);
			return serviceResponse;
		}
	}
}
