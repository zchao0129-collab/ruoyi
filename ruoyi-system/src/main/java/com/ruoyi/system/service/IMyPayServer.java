package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.domain.SysAlipayConfig;

public interface IMyPayServer {

    /**
     * 下单
     * 接口地址 /back-jar/open/trade/pay
     *
     * 请求方式 POST
     */
    AjaxResult tradeOrder(OrgOrderInfo orderInfo, SysAlipayConfig alipayConfig);

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


}
