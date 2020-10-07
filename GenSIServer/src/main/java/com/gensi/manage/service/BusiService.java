package com.gensi.manage.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gensi.manage.entity.RequestMsg;
import com.gensi.module.mobileArea.MobileAreaCallbackV2;
import com.gensi.module.phoneTag.PhoneTagCallbackV2;
import com.gensi.module.phoneTag.PhoneTagInfo;
import com.myserver.mobilearea.MobileAreaService;
import com.myserver.mobiletag.MobileTagService;
import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 具体处理业务逻辑
 */
@Service
public class BusiService {

	@Resource
	private TaskExecutor taskExecutor;
	@Resource
	private GsrequestService gsRequestService;
	
	@Reference(timeout=1000,retries=0,cluster="failfast")
	private MobileTagService mobileTagService;
	
	@Reference(timeout=1000,retries=0,cluster="failfast")
	private MobileAreaService mobileAresService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void doProcess(String sysId,String transId, String serviceCode, Map<String, Object> requestMsg) {
		logger.info("start doProcess:sysId => "+sysId+";serviceCode => "+serviceCode+";requestMsg => "+requestMsg);
		// =================================手机号码标注业务==========================================
		if (RequestMsg.MOBILE_MARK_SERVICE.equals(serviceCode)) {
			logger.info("process entry => 进入手机号码标注接口对接");
			 // 转换body为业务对象
			 PhoneTagInfo requestInfo = new PhoneTagInfo();
			 requestInfo.setTransId(transId);
			 requestInfo.setMobile(requestMsg.get("mobile").toString());
			 // 启动数据回调线程
			 taskExecutor.execute(new PhoneTagCallbackV2(sysId,requestInfo,gsRequestService,mobileTagService));
			 // 返回正确接收请求的响应信息
			 logger.info("process info : 手机号码标注异步推送线程正常启动");
			// =================================手机号码归属省==========================================	 
		}else if(RequestMsg.MOBILE_AREA_SERVICE.equals(serviceCode)){
			logger.info("process entry => 进入手机号码标注接口对接");
			 // 转换body为业务对象
			 PhoneTagInfo requestInfo = new PhoneTagInfo();
			 requestInfo.setTransId(transId);
			 requestInfo.setMobile(requestMsg.get("mobile").toString());
			 // 启动数据回调线程
			 taskExecutor.execute(new MobileAreaCallbackV2(sysId,requestInfo,gsRequestService,mobileAresService));
			 // 返回正确接收请求的响应信息
			 logger.info("process info : 手机号码标注异步推送线程正常启动");
		}else {
			logger.info("doProcess : entry => 没有匹配的接口，请确认serviceCode是否正确。");
		}
	}
}
