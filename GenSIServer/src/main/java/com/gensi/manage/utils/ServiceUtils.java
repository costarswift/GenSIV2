package com.gensi.manage.utils;

import com.gensi.manage.entity.GsmanageWithBLOBs;
import com.gensi.manage.entity.ServiceResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 服务工具类
 * 
 * @author shencheng
 *
 */
public class ServiceUtils {
	private static final Logger logger = Logger.getLogger(com.gensi.manage.utils.ServiceUtils.class);

	/**
	 * 向指定系统推送数据(同步方式)
	 * 
	 * @param sysId
	 *            系统标识
	 * @param data
	 *            要推送的数据，json格式
	 */
	public static ServiceResponseEntity send(String sysId, String data) {
		logger.info("同步推送报文:目标系统=>"+sysId+";报文内容 =》 "+data);
		return doSend(sysId, data);
	}

	/**
	 * 向指定系统推送数据(异步方式)
	 * 
	 * @param sysId
	 *            系统标识
	 * @param data
	 *            要推送的数据，json格式
	 */
	public static void sendAsyn(final String sysId, final String data) {
		logger.info("异步推送报文:目标系统=>"+sysId+";报文内容 =》 "+data);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				doSend(sysId, data);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 
	 * @param sysId
	 *            系统标识
	 * @param data
	 *            要推送的数据，json格式
	 * @param callback
	 *            异步响应完成后，对响应结果进行处理的方法
	 */
	public static void sendAsyn(final String sysId, final String data, final ServiceResponseCallback callback) {
		logger.info("异步推送报文:目标系统=>"+sysId+";报文内容 =》 "+data);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ServiceResponseEntity serviceResponseEntity = doSend(sysId, data);
				callback.processResponse(serviceResponseEntity);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 执行发送
	 * 
	 * @param sysId
	 * @param data
	 * @return
	 */
	private static ServiceResponseEntity doSend(String sysId, String data) {
		ServiceResponseEntity serviceResponseEntity = new ServiceResponseEntity();
		String message = "";
		Object responseData = null;
		try {
			// 获取系统信息
			GsmanageWithBLOBs gsmanage = ConfigUtils.getFtmanage(sysId);
			// 无效的系统标识
			if (gsmanage == null) {
				message = "无效的系统标识：" + sysId;
				serviceResponseEntity.setMessage(message);
				serviceResponseEntity.setSuccess(false);
				logger.warn(message);
				return serviceResponseEntity;
			}
			// 消息推送参数
			String notifyParam = gsmanage.getNotifyparam();
			// 执行推送
			if (StringUtils.isBlank(notifyParam)) {
				responseData = CommonUtils.sendHttpBodyRequest(gsmanage.getNotifyurl(), data);
			} else {
				responseData = CommonUtils.sendHttpParameterRequest(gsmanage.getNotifyurl(), notifyParam, data);
			}
			message = "发送成功";
			serviceResponseEntity.setSuccess(true);
		} catch (Exception e) {
			message = "发送异常";
			logger.error(message + " ," + e.getMessage(), e);
			serviceResponseEntity.setSuccess(false);
		}
		serviceResponseEntity.setMessage(message);
		serviceResponseEntity.setData(responseData);
		return serviceResponseEntity;
	}
}
