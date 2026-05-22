package com.ruoyi.web.controller.system;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.OrgPayChannel;
import com.ruoyi.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Controller
@RequestMapping("/system/order")
public class OrgOrderInfoController extends BaseController
{
    private String prefix = "system/order";

    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private IOrgPayChannelService orgPayChannelService;

    @RequiresPermissions("system:order:view")
    @GetMapping()
    public String order()
    {
        return prefix + "/order";
    }




    /**
     * 查询订单列表
     */
    @RequiresPermissions("system:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OrgOrderInfo orgOrderInfo)
    {
        startPage();
        SysUser currentUser = ShiroUtils.getSysUser();
        String loginName = currentUser.getLoginName();
        logger.info("loginName:"+loginName);
        if(!"admin".equals(loginName) && !"admin123".equals(loginName)){
            orgOrderInfo.setAccountName(loginName);
        }
        List<OrgOrderInfo> list = orgOrderInfoService.selectOrgOrderInfoList(orgOrderInfo);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @RequiresPermissions("system:order:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OrgOrderInfo orgOrderInfo)
    {
        List<OrgOrderInfo> list = orgOrderInfoService.selectOrgOrderInfoList(orgOrderInfo);
        ExcelUtil<OrgOrderInfo> util = new ExcelUtil<OrgOrderInfo>(OrgOrderInfo.class);
        return util.exportExcel(list, "订单数据");
    }

    /**
     * 新增订单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存订单
     */
    @RequiresPermissions("system:order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OrgOrderInfo orgOrderInfo)
    {
        return toAjax(orgOrderInfoService.insertOrgOrderInfo(orgOrderInfo));
    }

    /**
     * 修改订单
     */
    @RequiresPermissions("system:order:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        OrgOrderInfo orgOrderInfo = orgOrderInfoService.selectOrgOrderInfoById(id);
        mmap.put("orgOrderInfo", orgOrderInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存订单
     */
    @RequiresPermissions("system:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OrgOrderInfo orgOrderInfo)
    {
        return toAjax(orgOrderInfoService.updateOrgOrderInfo(orgOrderInfo));
    }

    /**
     * 删除订单
     */
    @RequiresPermissions("system:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orgOrderInfoService.deleteOrgOrderInfoByIds(ids));
    }

    /**
     * 回调订单
     */
    @RequiresPermissions("system:order:callbackOrder")
    @Log(title = "回调订单", businessType = BusinessType.UPDATE)
    @GetMapping("/callbackOrder/{id}")
    @ResponseBody
    public AjaxResult callbackOrder(@PathVariable("id") Long id)
    {
        OrgOrderInfo orgOrderInfo =  orgOrderInfoService.selectOrgOrderInfoById(id);
        if("success".equals(orgOrderInfoService.callbackOrder(orgOrderInfo))){
            return AjaxResult.success();
        }else{
            return AjaxResult.error("回调失败！");
        }
    }


    @RequiresPermissions("system:order:rechargeOrder")
    @GetMapping("/rechargeOrder/{orderNo}")
    public String rechargeOrder(@PathVariable("orderNo") String orderNo, ModelMap mmap)
    {
        mmap.put("orderNo", orderNo);
        OrgOrderInfo orgOrderInfo = orgOrderInfoService.selectorderByOrderId(orderNo);
        mmap.put("amount", orgOrderInfo.getAmount());
        return prefix + "/payOrder";
    }

    @RequiresPermissions("system:order:payOrder")
    @GetMapping("/payOrder/{orderNo}/{amount}")
    public String payOrder(@PathVariable("orderNo") String orderNo, @PathVariable("amount") BigDecimal amount, HttpServletRequest request)
    {
        String ipadd = getIpAddr(request);
        logger.info("IP Add :"+ipadd);
        return prefix + "/payOrder";
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

}
