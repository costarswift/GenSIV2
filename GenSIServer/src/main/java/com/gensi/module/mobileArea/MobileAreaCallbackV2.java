package com.gensi.module.mobileArea;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.config.GsLogConfig;
import com.gensi.manage.entity.RequestMsg;
import com.gensi.manage.entity.ServiceResponseHeader;
import com.gensi.manage.entity.ServiceResponseMsg;
import com.gensi.manage.service.GsrequestService;
import com.gensi.manage.utils.ServiceUtils;
import com.gensi.manage.utils.SpringContextUtil;
import com.gensi.module.phoneTag.PhoneTagInfo;
import com.myserver.mobilearea.MobileAreaService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MobileAreaCallbackV2 implements Runnable{

	private PhoneTagInfo phoneTagInfo;
	private GsrequestService gsRequestService;
	private String sysId;
	private MobileAreaService mobileAreaService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public MobileAreaCallbackV2() {}
	
	public MobileAreaCallbackV2(String sysId, PhoneTagInfo phoneTagInfo, GsrequestService gsRequestService, MobileAreaService mobileAreaService) {
		this.phoneTagInfo = phoneTagInfo;
		this.gsRequestService = gsRequestService;
		this.sysId = sysId;
		this.mobileAreaService = mobileAreaService;
	}
	@Override
	public void run() {
		SpringContextUtil.getBean(GsLogConfig.class).addTransLogAppender(logger, phoneTagInfo.getTransId());
		logger.info("start thread MobileAreaCallbackV2: phoneTagInfo  = " + JSONObject.toJSON(phoneTagInfo));

		Map<String, Object> paras = new HashMap<>();
		paras.put("transId", phoneTagInfo.getTransId());
		paras.put("serviceCode", RequestMsg.MOBILE_MARK_SERVICE);
		// 获取数据
		String desc = "";
		try {
			desc = mobileAreaService.getMobileArea(phoneTagInfo.getMobile());
			logger.debug("desc = " + desc);
			// 整理返回数据
			ServiceResponseMsg serviceResponse = new ServiceResponseMsg();
			
			ServiceResponseHeader serviceResponseHeader = new ServiceResponseHeader();
			serviceResponseHeader.setTransId(phoneTagInfo.getTransId());
			serviceResponseHeader.setIntime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			serviceResponseHeader.setServiceCode(RequestMsg.MOBILE_MARK_SERVICE);
			serviceResponse.setHeader(serviceResponseHeader);
			
			JSONObject rspBody = new JSONObject();
			rspBody.put("mobile", phoneTagInfo.getMobile());
			rspBody.put("desc", desc);
			serviceResponse.setBody(rspBody);
			
			// 本地保存服务记录
			Map<String, Object> rspInfo = new HashMap<>();
			rspInfo.put("transId", phoneTagInfo.getTransId());
			rspInfo.put("reqbody", JSON.toJSONString(phoneTagInfo));
			rspInfo.put("rspBody", JSONObject.toJSONString(serviceResponse));
			rspInfo.put("inTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			rspInfo.put("serviceCode", RequestMsg.MOBILE_AREA_SERVICE);
			gsRequestService.addRequestHis(rspInfo);

			// 发送数据
			ServiceUtils.send(this.sysId, JSONObject.toJSONString(serviceResponse));
		}catch(Exception e) {
			logger.error("查询手机号码归属地出错：",e);
		}
	}

}
