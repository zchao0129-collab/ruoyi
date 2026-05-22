package com.ruoyi.web.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TSafeTransfer;
import com.ruoyi.system.service.ITSafeTransferService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 安全发Controller
 *
 * @author ruoyi
 * @date 2025-04-21
 */
@Controller
@RequestMapping("/system/transfer")
public class TSafeTransferController extends BaseController
{
    private String prefix = "system/transfer";

    @Value(value = "${safeIssuedSetting.appId}")
    private String safeIssuedAppid;

    @Autowired
    private ITSafeTransferService tSafeTransferService;

    @RequiresPermissions("system:transfer:view")
    @GetMapping()
    public String transfer()
    {
        return prefix + "/transfer";
    }

    /**
     * 查询安全发列表
     */
    @RequiresPermissions("system:transfer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TSafeTransfer tSafeTransfer)
    {
        startPage();
        List<TSafeTransfer> list = tSafeTransferService.selectTSafeTransferList(tSafeTransfer);
        return getDataTable(list);
    }

    /**
     * 导出安全发列表
     */
    @RequiresPermissions("system:transfer:export")
    @Log(title = "安全发", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TSafeTransfer tSafeTransfer)
    {
        List<TSafeTransfer> list = tSafeTransferService.selectTSafeTransferList(tSafeTransfer);
        ExcelUtil<TSafeTransfer> util = new ExcelUtil<TSafeTransfer>(TSafeTransfer.class);
        return util.exportExcel(list, "安全发数据");
    }

    /**
     * 新增安全发
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存安全发
     */
    @RequiresPermissions("system:transfer:add")
    @Log(title = "安全发", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TSafeTransfer tSafeTransfer)
    {
        tSafeTransfer.setTimestamp(new Date());
        tSafeTransfer.setAppId(safeIssuedAppid);
        tSafeTransfer.setCreateTime(new Date());
        String id = IdWorkerUtil.getId()+"";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderNo = sdf.format(new Date())+id.substring(id.length()-10,id.length());
        tSafeTransfer.setOrderId(orderNo);
        return toAjax(tSafeTransferService.insertTSafeTransfer(tSafeTransfer));
    }

    /**
     * 修改安全发
     */
    @RequiresPermissions("system:transfer:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TSafeTransfer tSafeTransfer = tSafeTransferService.selectTSafeTransferById(id);
        mmap.put("tSafeTransfer", tSafeTransfer);
        return prefix + "/edit";
    }

    /**
     * 修改保存安全发
     */
    @RequiresPermissions("system:transfer:edit")
    @Log(title = "安全发", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TSafeTransfer tSafeTransfer)
    {
        return toAjax(tSafeTransferService.updateTSafeTransfer(tSafeTransfer));
    }

    /**
     * 删除安全发
     */
    @RequiresPermissions("system:transfer:remove")
    @Log(title = "安全发", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tSafeTransferService.deleteTSafeTransferByIds(ids));
    }


    /**
     * 提交安全发订单
     */
    @RequiresPermissions("system:transfer:sendTran")
    @GetMapping("/sendTran/{id}")
    @ResponseBody
    public AjaxResult sendTran(@PathVariable("id") Long id)
    {
        return tSafeTransferService.sendTSafeTransferByIds(id);
    }


    /**
     * 获取转账订单详情
     */
    @RequiresPermissions("system:transfer:getTranOrderInfo")
    @GetMapping("/getTranOrderInfo/{id}")
    @ResponseBody
    public AjaxResult getTranOrderInfo(@PathVariable("id") Long id)
    {
        return tSafeTransferService.queryTransfer(id);
    }


    /**
     * 查询余额
     */
    @RequiresPermissions("system:transfer:getAccountBalance")
    @GetMapping("/getAccountBalance")
    @ResponseBody
    public AjaxResult getAccountBalance()
    {
        return tSafeTransferService.queryBalance();
    }
}
