package com.gensi.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.config.GsLogConfig;
import com.gensi.manage.entity.GsResponse;
import com.gensi.manage.entity.RequestMsgHeader;
import com.gensi.manage.entity.RequestMsgHeaderV2;
import com.gensi.manage.service.BusiService;
import com.gensi.manage.service.DecrypService;
import com.gensi.manage.service.GsrequestService;
import com.gensi.manage.utils.ConfigUtils;
import com.gensi.manage.utils.ServiceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/genSI")
@Api("GenSI接口")
public class GenSIController {
	
	@Resource
	private GsrequestService gsrequestService;
	@Resource
	private BusiService busiService;
	@Resource
	private GsLogConfig gsLogConfig;
	@Resource
	private DecrypService decrypService;

	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 两个接口统一的处理逻辑：
	 * 1、接口接收请求，进行参数验证。
	 * 2、同步返回参数验证的结果。
	 * 3、参数验证通过后，进入业务处理逻辑。 根据transId查询历史请求，已经请求过的，直接返回上次的结果。
	 * 没有请求过的， 进行业务逻辑处理，异步推送业务结果到sysId对应的服务接收地址。
	 */
	@ApiOperation(value = "GenSI Restful接口", notes = "GenSI Restful调试接口")
	@RequestMapping(value = "/gsInterface", method = RequestMethod.POST)
	public Object gsRestInterface(@RequestBody String requestMessage, HttpServletRequest request) {
		logger.info("GsRestInterface: request=> " + requestMessage);
		JSONObject rspBody = new JSONObject();
		JSONObject jRequestMessage = JSONObject.parseObject(requestMessage);
		// 通用接口只接收通用的JSON对象，不对该对象做任何定义。
		RequestMsgHeader reqHeader = jRequestMessage.getObject("header", RequestMsgHeader.class);
		// JSONObject requestParaBody = jRequestMessage.getJSONObject("body");
		// 通一使用sysId标识时再启用
		// header为必须传的参数 包含transId,sysId,serviceCode。
		if (null == jRequestMessage || null == reqHeader) {
			logger.info("GsRestInterface: error => 接口数据错误");
			// 返回错误信息
			rspBody.put("transId", "");
			rspBody.put("result", "1");
			rspBody.put("desc", "接口数据错误");
			return rspBody;
		}
		// 启动mysql连接重连测试
		gsrequestService.connectCheck();
		// 统一鉴权预留。
		// 根据接口内容将body提取成接口业务需要的对象
		String serviceCode = reqHeader.getServiceCode();
		String reqTransId = reqHeader.getTransId();
		String sysId = reqHeader.getSysId();
		GsResponse gsRes = null;// 返回给外围系统的结果
		JSONObject jRequestBody = jRequestMessage.getJSONObject("body");
		// 检查系统标识是否存在 通一使用sysId标识时再启用
		if (!ConfigUtils.checkSysId(sysId)) {
			logger.info("GsRestInterface: error =>无效的系统标识sysId：" + sysId);
			// 返回错误信息
			gsRes = new GsResponse(serviceCode, reqTransId, "2", "无效的系统标识"+sysId);
			rspBody = gsRes.toJsonFormat();
			return rspBody;
		}
		//以ServiceCode和transId为标识，有历史记录就返回历史记录
		if(StringUtils.isEmpty(reqTransId)|| StringUtils.isEmpty(sysId)|| StringUtils.isEmpty(serviceCode)){
			logger.info("GsRestInterface: error => 缺少必要的参数");
			gsRes = new GsResponse(serviceCode, reqTransId, "3", "缺少必要的参数");
			rspBody = gsRes.toJsonFormat();
			return rspBody;
		}else{
			//追加该请求的日志文件
			gsLogConfig.addTransLogAppender(logger, reqTransId);
			logger.info("GsRestInterface: request=> " + requestMessage);
			//参数处理正常即同步返回请求接收成功的响应，业务结果异步进行推送
			logger.info("GsRestInterface: status => 参数处理正常");
			gsRes = new GsResponse(serviceCode, reqTransId, "0", "请求接收成功");
			rspBody = gsRes.toJsonFormat();
		
			String hisRsponse = gsrequestService.getHisResp(reqTransId, serviceCode);// 查询历史记录
			if (StringUtils.isNotEmpty(hisRsponse)) {
				// 有历史记录，直接给蜂易贷推送历史记录。
				logger.info("crawlerInterface: info => 	该请求已正常请求过，将返回历史结果;transId:"+reqTransId+";serviceCode:"+serviceCode+" ");
				// 异步发送给蜂易贷
				ServiceUtils.sendAsyn(sysId, hisRsponse);
				return rspBody;
			}
		}
		// 具体每个接口的业务逻辑
		busiService.doProcess(sysId,reqTransId, serviceCode, jRequestBody);

		logger.info("crawlerInterface: 返回请求响应  => " + rspBody.toJSONString());
		gsLogConfig.removeTransLogAppender(logger);
		return rspBody;
	}

	@ApiOperation(value = "GenSI加密接口", notes = "增加接口鉴权相关机制。需配合提供的客户端程序进行使用。")
	@RequestMapping(value = "/gsInterfaceV2", method = RequestMethod.POST)
	public Object gsDecrypedInterfaceV2(HttpServletRequest request) {
		logger.info("GenSIInterfaceV2: received Request => " + request.getParameter("ftRequestInfo"));
		JSONObject rspBody = new JSONObject();
		GsResponse fydRes = null;
		JSONObject ftRequestInfo = JSON.parseObject(request.getParameter("ftRequestInfo"));
		// 报文解密
		JSONObject jRequestMessage = decrypService.decrypData(ftRequestInfo, rspBody);
		logger.info("GenSIInterfaceV2: decryped Request=> " + jRequestMessage);
		// 解密失败 返回错误信息
		if (null == jRequestMessage) {
			return rspBody;
		}
		RequestMsgHeaderV2 reqHeader = jRequestMessage.getObject("header", RequestMsgHeaderV2.class);
		//简单报文参数检查
		if(null == reqHeader || "".equals(reqHeader.getServiceCode()) || "".equals(reqHeader.getTransId())){
			logger.info("crawlerInterface: error =>报文头参数缺失");
			// 返回错误信息
			rspBody.put("transId", "");
			rspBody.put("result", "1");
			rspBody.put("desc", "报文头参数缺失");
			return rspBody;
		}
		// 检查系统标识是否存在 通一使用sysId标识时再启用
		String sysId = reqHeader.getSysId();
		if (!ConfigUtils.checkSysId(sysId)) {
			logger.info("crawlerInterface: error =>无效的系统标识sysId：" + sysId);
			// 返回错误信息
			rspBody.put("transId", "");
			rspBody.put("result", "1");
			rspBody.put("desc", "无效的系统标识:" + sysId);
			return rspBody;
		}
		
		JSONObject jRequestBody = jRequestMessage.getJSONObject("body");
		String transId = reqHeader.getTransId();
		String serviceCode = reqHeader.getServiceCode();
		
		//有历史请求记录直接返回
		if(StringUtils.isEmpty(transId)|| StringUtils.isEmpty(sysId)|| StringUtils.isEmpty(serviceCode)){
			logger.info("crawlerInterface: error => 参数处理错误");
			fydRes = new GsResponse(serviceCode, transId, "1", "参数处理错误");
			rspBody = fydRes.toJsonFormat();
			return rspBody;
		}else{
			gsLogConfig.addTransLogAppender(logger, transId);
			logger.info("fydInterfaceV2: request=> " + jRequestMessage);
			
			logger.info("crawlerInterface: error => 参数处理正常");
			fydRes = new GsResponse(serviceCode, transId, "0", "请求接收成功");
			rspBody = fydRes.toJsonFormat();
			
			String hisRsponse = gsrequestService.getHisResp(transId, serviceCode);// 查询历史记录
			if (StringUtils.isNotEmpty(hisRsponse)) {
				// 有历史记录，直接推送历史记录。
				logger.info("crawlerInterface: info => transId:"+transId+";serviceCode:"+serviceCode+" 已正常请求过，返回历史结果");
				// 异步发送结果
				ServiceUtils.sendAsyn(sysId, hisRsponse);
				return rspBody;
			}
			//没有历史记录。if块执行完成， 执行后面的业务逻辑
		}
		
		busiService.doProcess(sysId,transId, serviceCode, jRequestBody);
		gsLogConfig.removeTransLogAppender(logger);
		return rspBody;
	}


}
