package com.gensi.manage.utils;

import com.gensi.manage.entity.ServiceResponseEntity;

/**
 * 服务发送回调
 * 
 */
public interface ServiceResponseCallback {
	/**
	 * 处理响应结果
	 * 
	 * @param serviceResponseEntity
	 */
	void processResponse(ServiceResponseEntity serviceResponseEntity);
}
