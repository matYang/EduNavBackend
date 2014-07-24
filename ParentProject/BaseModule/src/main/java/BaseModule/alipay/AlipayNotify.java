package BaseModule.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlipayNotify {

    /**
     * 检验反馈的sign
     * 
     * @param 通知返回来的参数数组
     * 
     * @return true or false
     */

    public static boolean isSignValide(Map<String, String> Params) {
        String sign = getSignFromMap(Params);
        String expectSign = GetMysign(Params);
        return sign.equals(expectSign);
    }

    /**
     * *功能：根据反馈回来的信息，生成签名结果
     * 
     * @param Params
     *            通知返回来的参数数组
     * @param key
     *            安全校验码
     * @return 生成的签名结果
     */
    public static String GetMysign(Map<String, String> Params) {
        Map<String, String> sParaNew = AlipayCore.ParaFilter(Params);// 过滤空值、sign与sign_type参数
        String mysign = AlipayCore.BuildMysign(sParaNew);// 获得签名结果

        return mysign;
    }

    /**
     * 得到notify_id
     * 
     * @param sPara
     * 
     */
    public static String getNotifyIdFromMap(Map<String, String> params) {
        List keys = new ArrayList(params.keySet());
        String notify_id = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);

            if (key.equals("notify_id")) {
                notify_id = value;
            }

        }
        return notify_id;
    }

    public static String getSignFromMap(Map<String, String> params) {
        List keys = new ArrayList(params.keySet());
        String sign = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);

            if (key.equals("sign")) {
                sign = value;
            }

        }
        return sign;
    }

    /**
     * *功能：获取远程服务器ATN结果,验证返回URL
     * 
     * @param notify_id
     *            通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String Verify(String notify_id) {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String partner = AlipayConfig.partner;
        String veryfy_url = "https://mapi.alipay.com/gateway.do?service=notify_verify";
        veryfy_url = veryfy_url + "&partner=" + partner + "&notify_id="
                + notify_id;
        String responseTxt = CheckUrl(veryfy_url);
        return responseTxt;
    }

    /**
     * *功能：获取远程服务器ATN结果
     * 
     * @param urlvalue
     *            指定URL路径地址
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static String CheckUrl(String urlvalue) {
        String inputLine = "";

        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputLine;
    }
}
