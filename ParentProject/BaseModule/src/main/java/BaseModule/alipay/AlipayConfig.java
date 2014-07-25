package BaseModule.alipay;

import org.restlet.data.Reference;

public class AlipayConfig {

    // 商家ID
    public static String partner = "2088511319628145";

    // 商户的私钥
    public static String key = "iwtilotw6cdxsvfw4qqk3n07feqplgh8";

    // 卖家支付宝用户号
    public static String seller_id = "2088511319628145";
    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    // 签名方式 不需修改
    public static String sign_type = "MD5";

    // 超时时间
    public static String it_b_pay = "1h";
    // notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
    public static String notify_url = "http://usertest.ishangke.cn/test-api/v1.0/alipay/alipay/notify_Url";

    // 付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
    // return_url的域名不能写成http://localhost/js_jsp_utf8/return_url.jsp，否则会导致return_url执行无效
    // public static String return_url =
    // "http://usertest.ishangke.cn/test-api/v1.0/alipay/returnUrl";
    public static String return_url = "";
    // payment_type 默认为1
    public static String payment_type = "1";

    // transport
    public static String transport = "https";

    public static String successRedirect = "http://usertest.ishangke.cn/alipay/success.html";

    public static String failureRedirect = "http://usertest.ishangke.cn/alipay/fail.html";

}