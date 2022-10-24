package com.myserver.mobiletag;

import com.alibaba.dubbo.config.annotation.Service;
import com.myserver.utils.CodeUtil;
import com.myserver.utils.HttpUtil;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/7
 * @description:
 **/
@Service
public class MobileTagServiceImpl implements  MobileTagService{
    @Override
    public String getMobileTag(String mobile) {
        System.out.println("====开始查询手机号码标识======");
        try {
            String tagSearchUrl = "https://www.sogou.com/reventondc/inner/vrapi?number=${mobile}&callback=show&isSogoDomain=0&objid=10001001";
            String s1 = HttpUtil.httpGet(tagSearchUrl.replace("${mobile}", mobile));
            return CodeUtil.decodeUnicode(s1.substring(s1.indexOf(":") + 1, s1.indexOf(",")).replace("\"", "").replace("\\\\", "\\"));
        }catch (Exception e){
            return "unknown";
        }
    }
}
