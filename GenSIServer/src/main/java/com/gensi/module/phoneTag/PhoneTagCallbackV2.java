package com.gensi.module.phoneTag;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.config.GsLogConfig;
import com.gensi.manage.entity.RequestMsg;
import com.gensi.manage.entity.ServiceResponseHeader;
import com.gensi.manage.entity.ServiceResponseMsg;
import com.gensi.manage.service.GsrequestService;
import com.gensi.manage.utils.ServiceUtils;
import com.gensi.manage.utils.SpringContextUtil;
import com.google.common.collect.Maps;
import com.myserver.mobiletag.MobileTagService;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 手机标注回调 根据sysId进行推送
 * 
 */
public class PhoneTagCallbackV2 implements Runnable {

	private PhoneTagInfo phoneTagInfo;
	private GsrequestService gsRequestService;
	private String sysId;
	private MobileTagService mobileTagService;

	private Logger logger = Logger.getLogger(this.getClass());

	public PhoneTagCallbackV2() {

	}

	public PhoneTagCallbackV2(String sysId,PhoneTagInfo phoneTagInfo, GsrequestService gsRequestService,MobileTagService mobileTagService) {
		this.phoneTagInfo = phoneTagInfo;
		this.gsRequestService = gsRequestService;
		this.sysId = sysId;
		this.mobileTagService = mobileTagService;
	}

	@Override
	public void run() {
		SpringContextUtil.getBean(GsLogConfig.class).addTransLogAppender(logger, phoneTagInfo.getTransId());
		logger.info("start thread: phoneTagInfo  = " + JSONObject.toJSON(phoneTagInfo));

		Map<String, Object> paras = Maps.newHashMap();
		paras.put("transId", phoneTagInfo.getTransId());
		paras.put("serviceCode", RequestMsg.MOBILE_MARK_SERVICE);
		// 获取数据
		String desc = "";
		try {
			desc = mobileTagService.getMobileTag(phoneTagInfo.getMobile());
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
			Map<String, Object> rspInfo = Maps.newHashMap();
			rspInfo.put("transId", phoneTagInfo.getTransId());
			rspInfo.put("reqbody", JSON.toJSONString(phoneTagInfo));
			rspInfo.put("rspBody", JSONObject.toJSONString(serviceResponse));
			rspInfo.put("inTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			rspInfo.put("serviceCode", RequestMsg.MOBILE_MARK_SERVICE);
			gsRequestService.addRequestHis(rspInfo);

			// 发送数据
			ServiceUtils.sendAsyn(this.sysId, JSONObject.toJSONString(serviceResponse));
		}catch(Exception e) {
			logger.error("查询手机号码标注出错：",e);
		}
	}

	/**
	 * unicode 转字符串
	 */
	private static String unicode2String(String unicode) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);
			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}
}
