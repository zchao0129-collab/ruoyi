package com.ruoyi.common.utils.pay;

import java.util.HashMap;
import java.util.Map;

public class hnapyUtils {

    final static String url  = "https://gateway.hnapay.com/website/scanPay.do";

    public String CreateOrderUrl(XinshengApi xinshengAp){

        Map<String,String> map = new HashMap<String,String>();
        map.put("version","2.0");
        map.put("tranCode","WS01");
        map.put("merId","");  //新生支付平台提供给商户的唯一ID 商户ID
        map.put("merOrderNum","");  //商户订单号
        map.put("tranAmt",""); //商户订单金额
        map.put("submitTime",""); //提交时间
        map.put("payType","QRCODE_B2C");//支付方式
        map.put("orgCode","WECHATPAY"); //目标资金机构代码
                                        //              WECHATPAY：微信支付
                                        //              ALIPAY：阿里支付
                                        //              TENPAY：腾讯支付
                                        //              JDPAY：京东支付
                                        //              UNIONPAY：银联二维码
        map.put("goodsName","");  //商品名称
        map.put("goodsDetail",""); //商品详情
        map.put("tranIP","");      //tranIP	交易IP
        map.put("notifyUrl","");  //商户异步通知地址  交易完成后，商户系统用于接收新  生支付平台异步通知的地址
        map.put("remark","新生支付"); //扩展字段
        map.put("weChatMchId",""); //报备编号   新生支付平台提供给指定商户的客户号
        map.put("payLimit",""); //支付方式限定
        map.put("identityType",""); //证件类型
        map.put("minAge",""); //最小买家年龄
        map.put("vasType","");  //服务类型
        map.put("holderName","");  //姓名
        map.put("identityCode","");//证件号码
        map.put("mobileNo",""); //手机号
        map.put("charset","1");//编码方式
        map.put("signType","4"); //        1：RSA
                                //        3：国密交易证书
                                //        4：国密密钥
        map.put("signMsg","");



        String strSign= "tranCode=["+xinshengAp.getTranCode()+"]" +
                "version=["+xinshengAp.getVersion()+"]" +
                "merId=["+xinshengAp.getMerId()+"]" +
                "submitTime=["+xinshengAp.getSubmitTime()+"]" +
                "merOrderNum=["+xinshengAp.getMerOrderNum()+"]" +
                "tranAmt=["+xinshengAp.getTranAmt()+"]" +
                "payType=["+xinshengAp.getPayType()+"]" +
                "orgCode=["+xinshengAp.getOrgCode()+"]" +
                "notifyUrl=["+xinshengAp.getNotifyUrl()+"]" +
                "charset=["+xinshengAp.getCharset()+"]" +
                "signType=["+xinshengAp.getSignType()+"]";

        return null;
    }

}
