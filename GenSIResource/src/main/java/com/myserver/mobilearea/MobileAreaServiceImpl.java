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
    public String getMobileArea(String mobile) throws Exception {
        String tagSearchUrl = "https://www.sogou.com/websearch/phoneAddress.jsp?phoneNumber=15874260244&cb=handlenumber&isSogoDomain=0";
        String s = HttpUtil.httpGet(tagSearchUrl.replace("${mobile}",mobile));
        return s.substring(s.indexOf("\"")+1,s.lastIndexOf("\""));
    }
}
