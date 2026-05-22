package com.ruoyi.web.controller.outside;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.CallBackModel;
import com.ruoyi.web.controller.outside.domain.NewShengTondCallbackModel;
import com.ruoyi.web.controller.outside.domain.OutsideOrderVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/outside/notify")
public class OutsideNotifyController extends BaseController {

    private String prefix = "system/order";


    @Value(value = "${safeIssuedSetting.appId}")
    private String safeIssuedAppid;
    @Value(value = "${safeIssuedSetting.appSecret}")
    private String safeIssuedappSecret;

    @Autowired
    private IOrgMerchantService orgMerchantService;

    @Autowired
    private PayZftServer payZftServer;

    @Autowired
    private  IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;
    @Autowired
    private ITSafeTransferService tSafeTransferService;
    @Autowired
    private IOrgChannelMerchantService orgChannelMerchantService;

    private static final Logger logger = LoggerFactory.getLogger(OutsideNotifyController.class);

    @PostMapping("/callback")
    @ResponseBody
    public String callback(@RequestBody CallBackModel callBackModel){

        OrgChannelMerchant ocm = orgChannelMerchantService.selectOrgChannelMerchantByMerchantNo(callBackModel.getMerchantBaseId());
        String  afterSign = ocm.getMerchantNo()+callBackModel.getOutTradeNo()+ocm.getMerchanKey();
        String sign = Md5Utils.hash(afterSign);
        if(sign.equals(callBackModel.getSign())){
            OrgOrderInfo order = orgOrderInfoService.selectorderByOrderId(callBackModel.getOutTradeNo());
            if("TRADE_SUCCESS".equals(callBackModel.getTradeState())){
//                String res = payZftServer.queryOrder(ocm.getMerchantNo(),callBackModel.getOutTradeNo(),sign);
//                if("success".equals(res)){
                    order.setOrderStatus(1L);
                    order.setCompletionTime(new Date());
                    orgOrderInfoService.updateOrgOrderInfo(order);
                    asyncThread(order);//异步回调
                    return "success";
//                }else{
//                    logger.info("--------------------------------不正常请求！-------------------------------");
//                    return "fail";
//                }
            }else{
                logger.info("--------------------------------未支付成功！-------------------------------");
                return "fail";
            }
        }else{
            return "fail";
        }
    }


    @PostMapping("/myCallback")
    @ResponseBody
    public String myCallback(CallBackModel callBackModel){
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfig(callBackModel.getAppid());
        String  afterSign = alipayConfig.getAPPID()+callBackModel.getOrderNo()+ callBackModel.getMerchantOrderNo()+callBackModel.getPayStatus()+callBackModel.getAmount()+callBackModel.getPayTime()+alipayConfig.getAlipayPublicKey();
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        if(sign.equals(callBackModel.getSign())){
            OrgOrderInfo order = orgOrderInfoService.selectorderByOrderId(callBackModel.getMerchantOrderNo());
            if("1".equals(callBackModel.getPayStatus())){
                order.setOrderStatus(1L);
                order.setCompletionTime(new Date());
                orgOrderInfoService.updateOrgOrderInfo(order);
                asyncThread(order);//异步回调
                return "success";
//                }else{
//                    logger.info("--------------------------------不正常请求！-------------------------------");
//                    return "fail";
//                }
            }else{
                logger.info("--------------------------------未支付成功！-------------------------------");
                return "fail";
            }
        }else{
            return "fail";
        }
    }



    @Async
    public void asyncThread(OrgOrderInfo orderInfo){
        logger.info("异步回调");
        orgOrderInfoService.callbackOrder(orderInfo);
    }

    @GetMapping("/returnUrl")
    @ResponseBody
    public String payOrder(@RequestBody CallBackModel callBackModel){



        return "调用失败";
    }



    @PostMapping("/safeTransferCallback")
    @ResponseBody
    public String safeTransferCallback(@RequestBody NewShengTondCallbackModel newShengTondCallbackModel){

        JSONObject json = (JSONObject) JSONObject.toJSON(newShengTondCallbackModel);
        String sginBefore = newShengTondCallbackModel.getSign();
        String sginAfter = getSign(json,safeIssuedappSecret);
        if(sginBefore.equals(sginAfter)){
            TSafeTransfer tSafeTransfer = tSafeTransferService.selectTSafeTransferByOrderId(newShengTondCallbackModel.getMsgBody().getid());
            tSafeTransfer.setStatus(newShengTondCallbackModel.getMsgBody().getTransferStatus());
            tSafeTransfer.setTransDate(newShengTondCallbackModel.getMsgBody().getTransferTime());
            tSafeTransferService.updateTSafeTransfer(tSafeTransfer);
            return "SUCCESS";
        }
        return "FAIL";
    }

    public static String getSignStr(JSONObject body, String secret) {
        body.remove("sign");
        // 将请求参数按照参数名ASCII码从小到大排序
        List<String> sortedKeys = new ArrayList<>(body.keySet());
        Collections.sort(sortedKeys);

        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys) {
            String value = String.valueOf(body.get(key));
            if (StringUtils.isNotBlank(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb + "&secret=" + secret;
    }

    public String getSign(JSONObject body, String secret) {
        String signStr = getSignStr(body, secret);
        return DigestUtils.md5Hex(signStr).toUpperCase();
    }
}
