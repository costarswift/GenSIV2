package com.gensi.manage.service;

import com.gensi.manage.entity.GsHisInfo;
import com.gensi.manage.mapper.GsrequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.springframework.transaction.interceptor.*;

@Service("gsrequestService")
public class GsrequestService {
	@Autowired
	private GsrequestMapper serviceMapper;

	/**
	 * mysql连接默认如果闲置超过8小时，连接会断掉。已经将数据库连接配置为断后重连。 但是断掉后第一次访问还是会报错。所以需要有这个方法调用一下数据库连接。
	 */
	public void connectCheck() {
		try {
			serviceMapper.connectCheck();
		} catch (Exception e) {

		}
	}

	public String getHisResp(String transId, String serviceCode) {
		String res = null;
		Map<String, Object> paras = new HashMap<>(4);
		paras.put("transId", transId);
		paras.put("serviceCode", serviceCode);
		Map<String, Object> hisData = serviceMapper.getSingleById(paras);
		// 查询历史记录，以transId为唯一标识，查过的就给历史记录，没查过的就去获取新记录。
		if (null != hisData && hisData.size() > 0) {
			// 返回历史记录
			res = hisData.containsKey("rspBody") ? hisData.get("rspBody").toString() : null;
		}
		return res;
	}

	public GsrequestMapper getServiceMapper() {
		return serviceMapper;
	}

	public int addRequestHis(Map<String, Object> paras) {
		return serviceMapper.add(paras);
	}

	public int addRequestHis(GsHisInfo ftoulHisInfo) {
		return serviceMapper.addV2(ftoulHisInfo);
	}

	public List<Map<String, Object>> getAll(Map<String, Object> paras) {
		return serviceMapper.getAll(paras);
	}

	public Map<String, Object> getRequestHis(Map<String, Object> paras) {
		return serviceMapper.getSingleById(paras);
	}

	public int updateResp(Map<String, Object> paras) {
		return serviceMapper.updateRespByTransId(paras);
	}

	public int updateRespByPK(Map<String, Object> paras) {
		return serviceMapper.updateRespByPK(paras);
	}

	public int updateRespByTransId(Map<String, Object> paras) {
		return serviceMapper.updateRespByTransId(paras);
	}

	public int deleteByPK(Map<String, Object> rspInfo) {
		return serviceMapper.deleteByPK(rspInfo);
	}

}
