package mobileInfo;

import com.myserver.utils.HttpUtil;
import sun.net.www.http.HttpClient;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/7
 * @description:
 **/
public class mobileInfoTest {

    public static void main(String[] args) throws Exception {
//        String s = HttpUtil.httpGet("https://www.sogou.com/websearch/phoneAddress.jsp?phoneNumber=15874260244&cb=handlenumber&isSogoDomain=0");
//        System.out.println(s.substring(s.indexOf("\"")+1,s.lastIndexOf("\"")));
        String s1 = HttpUtil.httpGet("https://www.sogou.com/reventondc/inner/vrapi?number=15874260244&callback=show&isSogoDomain=0&objid=10001001");
        System.out.println(decodeUnicode(s1.substring(s1.indexOf(":") + 1, s1.indexOf(",")).replace("\"", "").replace("\\\\", "\\")));
        String test = "\u8be5\u53f7\u7801\u6682\u65e0\u6807\u8bb0";
        System.out.println(test);
    }

    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
}
