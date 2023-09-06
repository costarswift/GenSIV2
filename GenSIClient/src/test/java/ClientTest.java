import com.alibaba.fastjson.JSONObject;
import com.ftoul.ftoulClient.FtClient;
import com.ftoul.ftoulClient.ReqHeader;

/**
 * @author ：楼兰
 * @description:
 **/
public class ClientTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");

        FtClient ftClient = new FtClient();
        //一、基础参数部分
        //服务提供地址
        ftClient.setEndPoint("http://localhost:8887/genSI/gsInterfaceAsync");
//        ftClient.setEndPoint("http://localhost:8887/genSI/gsInterfaceSync");
        //以下三个根据服务处理时长灵活配置。不设也行， 有默认值。
        ftClient.setConnectionRequestTimeout(1000);
        ftClient.setConnectTimeout(30000);
        ftClient.setSocketTimeout(30000);
        //向第三方系统分配
        ftClient.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkii0/acnSZsnNS8KYcnyFJIdKuWWNwNGeiVAuFoOtuwTy9Ff5m5Hg2AaVeFEQ04/iTLvQj0XOdkl8AYsXpte/wAR3q4XLpFulgaUM5DTgrc9lBJQe/0wWiCoINGqYPj2cTni3jSuZZcPqvqeWCZVuYbXp+p5JSkDWuaIbOFVzR0/mAgtqnnQPG3qz6033KO975sNObIjtRM3idVfb2Rm/sYKLmj+Ac5FkcHfj8QuoyZ6RYzobOhHfL19bwy976Yp0aHP2nhMqSD1fQXxB8B9PDq6sDs1+xcrTDpY0GBfGFAs3gqZ0+MrP/Zrbm/+RyHDhzd+IzE4K6/n5l9VyQv5YQIDAQAB");
        String ftRes = "";
        //二、header参数部分
        ReqHeader reqHeader = new ReqHeader();
        //向第三方系统分配
        reqHeader.setSysId("GenSIClient");
        //用户名，密码，暂时无验证方案。预留。
        reqHeader.setSysUser("testUser");
        reqHeader.setSysPwd("testPwd");
        //唯一的请求ID。 重复的transId将直接返回第一次请求的结果。
        reqHeader.setTransId("20211112015278");
        //服务功能码-查询手机号码标注
//        reqHeader.setServiceCode("search_mobile_mark");
        reqHeader.setServiceCode("search_mobile_area");
        ftClient.setReportHeader(reqHeader);

        //三、业务参数部分 由提供的接口文档确定
        JSONObject reportbody = new JSONObject();
        reportbody.put("mobile", "15874260244");
        ftClient.setReportBody(reportbody.toJSONString());
        ftRes = ftClient.postRequest();
        //同步返回请求接收结果。具体的业务结果将向sysId配置的返回路径进行推送。
        System.out.println(ftRes);
    }
}