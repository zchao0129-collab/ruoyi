package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.OrgAccount;
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
import com.ruoyi.system.domain.AlipayUserInfo;
import com.ruoyi.system.service.IAlipayUserInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支付宝用户信息Controller
 *
 * @author ruoyi
 * @date 2023-08-19
 */
@Controller
@RequestMapping("/system/alipayUserInfo")
public class AlipayUserInfoController extends BaseController
{
    private String prefix = "system/alipayUserInfo";

    @Autowired
    private IAlipayUserInfoService alipayUserInfoService;

    @RequiresPermissions("system:alipayUserInfo:view")
    @GetMapping()
    public String alipayUserInfo()
    {
        return prefix + "/alipayUserInfo";
    }

    /**
     * 查询支付宝用户信息列表
     */
    @RequiresPermissions("system:alipayUserInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AlipayUserInfo alipayUserInfo)
    {
        startPage();
        List<AlipayUserInfo> list = alipayUserInfoService.selectAlipayUserInfoList(alipayUserInfo);
        return getDataTable(list);
    }

    /**
     * 导出支付宝用户信息列表
     */
    @RequiresPermissions("system:alipayUserInfo:export")
    @Log(title = "支付宝用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AlipayUserInfo alipayUserInfo)
    {
        List<AlipayUserInfo> list = alipayUserInfoService.selectAlipayUserInfoList(alipayUserInfo);
        ExcelUtil<AlipayUserInfo> util = new ExcelUtil<AlipayUserInfo>(AlipayUserInfo.class);
        return util.exportExcel(list, "支付宝用户信息数据");
    }

    /**
     * 新增支付宝用户信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存支付宝用户信息
     */
    @RequiresPermissions("system:alipayUserInfo:add")
    @Log(title = "支付宝用户信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AlipayUserInfo alipayUserInfo)
    {
        alipayUserInfo.setUpdateTime(new Date());
        return toAjax(alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo));
    }

    /**
     * 修改支付宝用户信息
     */
    @RequiresPermissions("system:alipayUserInfo:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        AlipayUserInfo alipayUserInfo = alipayUserInfoService.selectAlipayUserInfoById(id);
        mmap.put("alipayUserInfo", alipayUserInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存支付宝用户信息
     */
    @RequiresPermissions("system:alipayUserInfo:edit")
    @Log(title = "支付宝用户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AlipayUserInfo alipayUserInfo)
    {
        return toAjax(alipayUserInfoService.updateAlipayUserInfo(alipayUserInfo));
    }

    /**
     * 删除支付宝用户信息
     */
    @RequiresPermissions("system:alipayUserInfo:remove")
    @Log(title = "支付宝用户信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(alipayUserInfoService.deleteAlipayUserInfoByIds(ids));
    }


    @RequiresPermissions("system:alipayUserInfo:changeIszt")
    @PostMapping("/changeIszt")
    @ResponseBody
    public AjaxResult changeIszt(AlipayUserInfo alipayUserInfo)
    {
        return toAjax(alipayUserInfoService.updateAlipayUserInfo(alipayUserInfo));
    }
}
