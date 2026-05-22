package com.ruoyi.web.controller.outside;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpRequest;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.security.RSAUtil;
import com.ruoyi.common.utils.security.SignStrUtil;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.domain.OrgChannelMerchant;
import com.ruoyi.system.domain.OrgOrderInfo;

import com.ruoyi.system.domain.SysAlipayConfig;
import com.ruoyi.system.mapper.OrgAccountMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.*;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/outside/order")
public class OutsideController extends BaseController {
    private String prefix = "system/order";


    @Autowired
    private AlipayServer alipayServer;
    @Autowired
    private IOrgOrderInfoService orderService;
    @Autowired
    private IOrgAccountService accountService;
    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;

    @Autowired
    private  IOrgChannelMerchantService ocmService;


    private static final Logger logger = LoggerFactory.getLogger(OutsideController.class);


    @PostMapping("/createOrder")
    @ResponseBody
    public AjaxResult createOrder(@RequestBody OutsideOrderVO orderVo) throws Exception{
        return new AjaxResult(AjaxResult.Type.ERROR,"调用失败！","");
    }


    @GetMapping("/payOrder/{id}")
    @ResponseBody
    public String payOrder(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id)){
            return "调用失败";
        }
        try{
            String strId = HexUtil.decodeHexStr(id);
            logger.info("解密后的串："+strId);
            if(strId.indexOf("_")>0){
                String[] ids = strId.split("_");
                OrgOrderInfo order = new OrgOrderInfo();
                order.setOrderNo(ids[0]);
                order.setAccountOrderNo(ids[1]);
                order.setAmount(new BigDecimal(ids[2]));
                order = orderService.queryOrder(order);
                return order.getBodys();
            }else{
                return "调用失败";
            }
        }catch (Exception e){
            logger.error("解密失败：" + e.getMessage());
            return "调用失败";
        }
    }


    @PostMapping("/queryOrder")
    @ResponseBody
    public String queryOrder(@RequestBody OutsideOrderVO orderVo){
        logger.info("接收参数  :"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return "fail";
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();
        logger.info("after  Sign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(orderVo.getSign())){
            return "fail";
        }
        OrgOrderInfo order = new OrgOrderInfo();
        order.setAccountOrderNo(orderVo.getMerchantOrderNo());
        order.setAmount(orderVo.getAmount());
        order.setAccountAppId(orderVo.getAppid());
        order = orderService.queryOrder(order);
        if(ObjUtil.isEmpty(order)){
            return "fail";
        }
        if(order.getOrderStatus()==1){
            return "success";
        }else {
            return "fail";
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    public  boolean getsign(OrgOrderInfo ooi,String toSign){
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(ooi.getAccountAppId());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return false;
        }
        String signstr = ooi.getOrderNo()+account.getAccountToken();
        String sign = Md5Utils.hash(signstr).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(toSign)){
            return true;
        }else {
            return false;
        }
    }

    @PostMapping("/queryJsonOrder")
    @ResponseBody
    public AjaxResult queryJsonOrder(@RequestBody OutsideOrderVO orderVo){
        logger.info("接收参数  :"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return AjaxResult.success("fail");
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();
        logger.info("after  Sign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(orderVo.getSign())){
            return AjaxResult.success("fail");
        }
        OrgOrderInfo order = new OrgOrderInfo();
        order.setAccountOrderNo(orderVo.getMerchantOrderNo());
        order.setAmount(orderVo.getAmount());
        order.setAccountAppId(orderVo.getAppid());
        order = orderService.queryOrder(order);
        if(ObjUtil.isEmpty(order)){
            return AjaxResult.success("fail");
        }
        if(order.getOrderStatus()==1){
            return AjaxResult.success("success");
        }else {
            return AjaxResult.success("fail");
        }
    }

}
