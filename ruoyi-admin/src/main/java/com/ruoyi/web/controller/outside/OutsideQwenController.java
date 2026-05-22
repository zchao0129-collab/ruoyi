package com.ruoyi.web.controller.outside;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.OutsideOrderVO;
import com.ruoyi.web.controller.outside.domain.OutsideQwenVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/outside/order/qwen")
public class OutsideQwenController extends BaseController {

    @Autowired
    private IOrgOrderInfoService orderService;
    @Autowired
    private AlipayServer alipayServer;

    @Autowired
    private IOrgAccountService accountService;

    @Autowired
    private QwenService qwenService;

    @PostMapping("/ask")
    @ResponseBody
    public AjaxResult askQuestion(@RequestBody OutsideQwenVO qwenVO) throws IOException {
        logger.info("接收参数:qwenVO.toString ：======>>"+qwenVO.toString());

        return qwenService.getAnswer(qwenVO.getAsk());
    }

    // 获取问题的详细回答（需要支付验证）
    @PostMapping("/ask/detail")
    @ResponseBody
    public AjaxResult getDetailedAnswer(@RequestBody OutsideQwenVO qwenVO) throws IOException {
        logger.info("接收参数:qwenVO.toString ：======>>"+qwenVO.toString());
        if (orderService.verifyPayment(qwenVO.getOrderNo())) {
            return qwenService.getAnswer(qwenVO.getQuestion());
        } else {
            return new AjaxResult(AjaxResult.Type.ERROR, "支付失败", null);
        }
    }



    @PostMapping("/createOrderInfo")
    @ResponseBody
    public AjaxResult createAlipayOrder(@RequestBody OutsideOrderVO orderVo) throws Exception{
        logger.info("接收参数:"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return new AjaxResult(AjaxResult.Type.ERROR,"appid为空","appid为空");
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return new AjaxResult(AjaxResult.Type.ERROR,"客户通道停用！","");
        }
        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getCallbackUrl()+
                orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();
        logger.info("afterSign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(orderVo.getSign())){
            return new AjaxResult(AjaxResult.Type.ERROR,"验签失败！","");
        }
        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setUid(orderVo.getUid());
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setAmount(orderVo.getAmount());
        String orderNo = getGeneralOrder();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setSubject("智能体支付");//sdf1
        orderInfo.setAcountAppId(orderVo.getAppid());
        orderInfo.setCashier(account.getCashier());
        orderInfo.setCallbackStatus(0L);
        return alipayServer.aliJSapiPayment(orderInfo);
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }
}
