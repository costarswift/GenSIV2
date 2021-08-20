package com.gensi.module.mobileArea;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gensi.manage.config.GsLogConfig;
import com.gensi.manage.entity.RequestMsg;
import com.gensi.manage.entity.ServiceResponseHeader;
import com.gensi.manage.entity.ServiceResponseMsg;
import com.gensi.manage.service.GsrequestService;
import com.gensi.manage.utils.SpringContextUtil;
import com.gensi.module.phoneTag.PhoneTagInfo;
import com.google.common.collect.Maps;
import com.myserver.mobilearea.MobileAreaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/7
 * @description:
 **/

@Service
public class PhoneAreaService {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private GsrequestService gsRequestService;
    @Reference(timeout=1000,retries=0,cluster="failfast")
    private MobileAreaService mobileAreaService;

    public ServiceResponseMsg service(PhoneTagInfo phoneTagInfo){
        SpringContextUtil.getBean(GsLogConfig.class).addTransLogAppender(logger, phoneTagInfo.getTransId());
        logger.info("start thread: phoneTagInfo  = " + JSONObject.toJSON(phoneTagInfo));

        Map<String, Object> paras = Maps.newHashMap();
        paras.put("transId", phoneTagInfo.getTransId());
        paras.put("serviceCode", RequestMsg.MOBILE_AREA_SERVICE);
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
            serviceResponseHeader.setServiceCode(RequestMsg.MOBILE_AREA_SERVICE);
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
            rspInfo.put("serviceCode", RequestMsg.MOBILE_AREA_SERVICE);
            gsRequestService.addRequestHis(rspInfo);

            return serviceResponse;
        }catch(Exception e) {
            logger.error("查询手机号码归属出错：",e);
        }
        return null;
    }
}
