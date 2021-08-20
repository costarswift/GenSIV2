package com.gensi.manage.entity;

/**
 * 服务响应实体类
 * 
 */
public class ServiceResponseEntity {
	/**
	 * 响应结果状态成功返回true,失败返回false
	 */
	private boolean success;

	/**
	 * 响应结果状态结果说明
	 */
	private String message;

	/**
	 * 响应结果中的业务数据
	 */
	private Object data;

	/**
	 * 以字符串形式返回返回数据
	 * 
	 * @return
	 */
	public String getDataAsString() {
		return this.data.toString();
	}

	/**
	 * 以java实体类对象形式返回数据
	 * 
	 * @param targetClass
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDataAsJavaBean(Class<T> targetClass) {
		if (targetClass == null) {
			throw new IllegalArgumentException("targetClass is null,要转成的目标类型不能是null");
		}
		if (this.data == null) {
			return null;
		}
		if (targetClass != this.data.getClass()) {
			return null;
		}
		return (T) this.data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
