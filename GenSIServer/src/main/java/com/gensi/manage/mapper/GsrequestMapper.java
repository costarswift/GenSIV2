package com.gensi.manage.mapper;

import com.gensi.manage.entity.GsHisInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GsrequestMapper {
	@Select("select 1")
    public String connectCheck();

    public int add(Map<String, Object> paras);
    public int addV2(GsHisInfo ftoulHisInfo);

    public List<Map<String, Object>> getAll(Map<String, Object> paras);

    public Map<String, Object> getById(Map<String, Object> paras);

    public Map<String, Object> getSingleById(Map<String, Object> paras);

    public int updateRespByTransId(Map<String, Object> paras);
    public int updateRespByTransIdV2(GsHisInfo ftoulHisInfo);

    public int updateRespByPK(Map<String, Object> paras);
    public int updateRespByPKV2(GsHisInfo ftoulHisInfo);

    public int deleteByPK(Map<String, Object> rspInfo);
}