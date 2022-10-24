package com.gensi.manage.entity;

public class RequestMsg {
    public static final String MOBILE_MARK_SERVICE = "search_mobile_mark";// 手机号码标注接口
    public static final String MOBILE_AREA_SERVICE = "search_mobile_area"; //手机号码归属度查询接口 2017年7月19日
    public static final String IDCARD_INFO_BAIDU = "search_idcard_info_baidu";//从百度查询身份证信息接口
    
    public RequestMsgHeader header;
    public RequestMsgBody body;

    public RequestMsgHeader getHeader() {
        return header;
    }

    public void setHeader(RequestMsgHeader header) {
        this.header = header;
    }

    public RequestMsgBody getBody() {
        return body;
    }

    public void setBody(RequestMsgBody body) {
        this.body = body;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestMsg [header=" + header + ", body=" + body + "]";
    }

}
