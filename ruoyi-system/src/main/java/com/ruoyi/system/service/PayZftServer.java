package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.OrgOrderInfo;

import java.io.IOException;

public interface PayZftServer {

    /**
     * 下单
     * 接口地址 /back-jar/open/trade/pay
     *
     * 请求方式 POST
     */
    AjaxResult tradeOrder(OrgOrderInfo orderInfo);

    String tradeGameOrder(OrgOrderInfo orderInfo);
    /**
     * 订单查询
     * 接口地址 /back-jar/open/trade/info
     *
     * 请求方式 POST
     */
    String queryOrder(String merchantId,String merchantTradeNo,String sign);

    /**
     *   退款
     *  接口地址 /back-jar/open/trade/refund
     *
     *  请求方式 POST
     */
   AjaxResult refundOrder(OrgOrderInfo orderInfo);


    /**
     *   回调
     */
    AjaxResult notifyOrder();


    AjaxResult yujianTradeOrder(OrgOrderInfo orderInfo) throws IOException;

    String yujianTradeGameOrder(OrgOrderInfo orderInfo);

    String yujianQueryOrder(String merchantId,String merchantTradeNo,String sign);
}
