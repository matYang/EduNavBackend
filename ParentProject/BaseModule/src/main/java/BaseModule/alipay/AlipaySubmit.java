package BaseModule.alipay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipaySubmit {

    public static String BuildForm(String out_trade_no, String subject,
            String total_fee) {
        Map sPara = new HashMap();
        sPara.put("service", "create_direct_pay_by_user");
        sPara.put("partner", AlipayConfig.partner);
        sPara.put("sign_type", AlipayConfig.sign_type);
        sPara.put("notify_url", AlipayConfig.notify_url);
        sPara.put("return_url", AlipayConfig.return_url);
        sPara.put("out_trade_no", out_trade_no);
        sPara.put("subject", subject);
        sPara.put("payment_type", AlipayConfig.payment_type);
        sPara.put("total_fee", total_fee);
        sPara.put("seller_id", AlipayConfig.seller_id);
        Map sParaNew = AlipayCore.ParaFilter(sPara); // 除去数组中的空值和签名参数
        String mysign = AlipayCore.BuildMysign(sParaNew);// 生成签名结果

        StringBuffer sbHtml = new StringBuffer();
        List keys = new ArrayList(sParaNew.keySet());
        Collections.sort(keys);
        String gateway = "https://mapi.alipay.com/gateway.do?";

        // GET方式传递
        // sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
        // + gateway + "_input_charset=" + input_charset +
        // "\" method=\"get\">");
        // POST方式传递（GET与POST二必选一）
        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
                + gateway
                + "_input_charset="
                + AlipayConfig.input_charset
                + "\" method=\"get\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaNew.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name
                    + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"hidden\" name=\"sign\" value=\"" + mysign
                + "\"/>");
        sbHtml.append("<input type=\"hidden\" name=\"sign_type\" value=\""
                + AlipayConfig.sign_type + "\"/>");

        // submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"支付宝确认付款\"></form>");

        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
}
