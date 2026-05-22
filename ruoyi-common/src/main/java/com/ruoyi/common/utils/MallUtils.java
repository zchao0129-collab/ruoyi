/*
package com.ruoyi.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

*/
/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年05月04日 16:10
 *//*

public class MallUtils {

    private static final Logger log = LoggerFactory.getLogger(MallUtils.class);
    private static final String pxu = "https://124.248.70.202:20710";

    //登录
    public static String getMallToken(){
        String url = pxu+"/api/front/login/mobile/password";
        String phone = "19119290099";
        String password = "123qwe";
        String pamar = "{\"phone\":\""+phone+"\",\"password\":\""+password+"\"}";
        String res = HttpUtil.post(url,pamar);
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        JSONObject dataObj = JSONObject.parseObject(data);
        String token = dataObj.getString("token");
       return token;
    }
//获取支付内容
    public static String getAlipayBody(){
        String url = pxu+"/api/front/pay/payment";
        String param="{\"orderNo\":\""+createOrder()+"\",\"payChannel\":\"alipay\",\"payType\":\"alipay\",\"scene\":0}";
        String res = HttpUtils.sendPost(url, param,"utf-8",getMallToken());
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        Integer code = jsonObject.getInteger("code");
        if(code != 200){
            log.info("查询商品失败！");
            return "";
        }
        JSONObject dataObj = JSONObject.parseObject(data);
        log.info("data："+data);
        return "";
    }
//创建订单
    public static String createOrder(){
        String url = pxu+"/api/front/order/create";
        String param="{" +
                "\"addressId\": 9," +
                "\"orderMerchantRequestList\": [{" +
                "\"shippingType\": 1," +
                "\"merId\": 2," +
                "\"remark\": \"\"," +
                "\"userCouponId\": 0" +
                "}]," +
                "\"isUseIntegral\": false," +
                "\"preOrderNo\": \""+getorderNo()+"\"" +
                "}";
        String res = HttpUtils.sendPost(url, param,"utf-8",getMallToken());
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        Integer code = jsonObject.getInteger("code");
        if(code != 200){
            log.info("查询商品失败！");
            return "";
        }
        JSONObject dataObj = JSONObject.parseObject(data);
        String orderNo =dataObj.getString("orderNo");
        BigDecimal payPrice = dataObj.getBigDecimal("payPrice");
        log.info("orderNo:"+orderNo);
        log.info("payPrice:"+payPrice);
        return orderNo;
    }
    public static String createAddress(){

        return "success";
    }
    //创建用户名 密码
    public static String createPhonePasswd(String phone,String pwd){
        String url = pxu+"/api/front/login/mobile/create";
        String param="{" +
                "\"phone\":\""+phone+"\"," +
                "\"password\":\""+pwd+"\"" +
                "}";
        String res = HttpUtils.sendPost(url, param,"utf-8",getMallToken());
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        Integer code = jsonObject.getInteger("code");
        if(code != 200){
            log.info("创建用户失败！");
            return "";
        }
        return "success";
    }

    //获取产品信息
    public static String getProductValue(Integer productId){
        String url = pxu+"20710/api/front/product/detail/"+productId+"?type=normal";
        String res = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        Integer code = jsonObject.getInteger("code");
       if(code != 200){
            log.info("查询商品失败！");
            return "";
        }
        JSONObject dataObj = JSONObject.parseObject(data);
        String productValueStr = dataObj.getString("productValue");
        JSONArray array = JSONObject.parseArray(dataObj.getString("productAttr"));
        JSONObject productAttr ;
        if(array.size()>0){
            productAttr = (JSONObject) array.get(0);
            log.info("attrValues:----"+productAttr.getString("attrValues"));
            String str = productAttr.getString("attrValues");
            JSONObject productValObj = JSONObject.parseObject(productValueStr);
            JSONObject productVal = JSONObject.parseObject(productValObj.getString(str));
            log.info("productVal:----id:"+productVal.getString("id"));
            log.info("productVal:----productId:"+productVal.getString("productId"));
            log.info("productVal:----price:"+productVal.getString("price"));
            return productValObj.getString(str);
        }
        return "";
    };


    //获取订单编号
    public static String getorderNo(){
        String url = pxu+"/api/front/order/pre/order";
        String productValue = getProductValue(71);
        JSONObject productValueJson = JSONObject.parseObject(productValue);
        Integer attrValueId = productValueJson.getInteger("id");
        Integer productId = productValueJson.getInteger("productId");
        String param = "{\"preOrderType\":\"buyNow\",\"orderDetails\":[{\"attrValueId\":"+attrValueId+",\"productId\":"+productId+",\"productNum\":1}]}";
        String res = HttpUtils.sendPost(url, param,"utf-8",getMallToken());
        JSONObject jsonObject = JSONObject.parseObject(res);
        Integer code = jsonObject.getInteger("code");
        if(code != 200) {
            log.info("下单失败！");
        }
        String data = jsonObject.getString("data");
        JSONObject dataObject = JSONObject.parseObject(data);
        String orderNo = dataObject.getString("orderNo");
        log.info(orderNo);
        return orderNo;
    }



    public static void main(String[] args) {
         //log.info(createPhonePasswd("19119290099","123qwe"));
        log.info(getMallToken());
    };

}
*/
