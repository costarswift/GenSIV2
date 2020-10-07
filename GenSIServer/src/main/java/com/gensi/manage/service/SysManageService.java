package com.gensi.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.entity.GsmanageExample;
import com.gensi.manage.entity.GsmanageWithBLOBs;
import com.gensi.manage.mapper.GsmanageMapper;
import com.gensi.manage.utils.RsaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysManageService {

	@Resource
	private GsmanageMapper gsmanageMapper;

	public int createSys(GsmanageWithBLOBs gsmanage) {
		if(StringUtils.isEmpty(gsmanage.getPrivatekey()) || StringUtils.isEmpty(gsmanage.getPublickey())){
			JSONObject newRsaKey = RsaUtils.generateKeyPairForJava();
			gsmanage.setPublickey(newRsaKey.getString("publicKey"));
			gsmanage.setPrivatekey(newRsaKey.getString("privateKey"));
		}
		int res = 0 ; 
		try{
			res = gsmanageMapper.insert(gsmanage);
		}catch(Exception e){
			res = gsmanageMapper.updateByPrimaryKey(gsmanage);
		}
		return res;
	}

	public List<GsmanageWithBLOBs> queryAllSys() {
		List<GsmanageWithBLOBs> res = null;
		GsmanageExample example = new GsmanageExample();
		example.setOrderByClause("sysId");
		res = gsmanageMapper.selectByExampleWithBLOBs(example);
		return res;
	}

	public int deleteSysById(String sysid) {
		return gsmanageMapper.deleteByPrimaryKey(sysid);
	}
}
