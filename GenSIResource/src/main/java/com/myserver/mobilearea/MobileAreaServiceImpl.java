package com.myserver.mobilearea;

import com.alibaba.dubbo.config.annotation.Service;
import com.myserver.utils.HttpUtil;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/7
 * @description:
 **/
@Service
public class MobileAreaServiceImpl implements MobileAreaService{
    @Override
    public String getMobileArea(String mobile) {
        System.out.println("====开始查询手机号码归属地======");
        try {
            String tagSearchUrl = "https://www.sogou.com/websearch/phoneAddress.jsp?phoneNumber=${mobile}&cb=handlenumber&isSogoDomain=0";
            String s = HttpUtil.httpGet(tagSearchUrl.replace("${mobile}",mobile));
            return s.substring(s.indexOf("(")+1,s.lastIndexOf(")"));
        }catch (Exception e){
            return "unknown";
        }

    }
}
