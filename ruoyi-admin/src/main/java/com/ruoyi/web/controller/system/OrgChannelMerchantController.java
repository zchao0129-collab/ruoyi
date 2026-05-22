package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.system.domain.OrgMerchant;
import com.ruoyi.system.domain.OrgPayChannel;
import com.ruoyi.system.service.IOrgMerchantService;
import com.ruoyi.system.service.IOrgPayChannelService;
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
import com.ruoyi.system.domain.OrgChannelMerchant;
import com.ruoyi.system.service.IOrgChannelMerchantService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商户通道Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Controller
@RequestMapping("/system/channelMerchant")
public class OrgChannelMerchantController extends BaseController
{
    private String prefix = "system/channelMerchant";
    @Autowired
    private IOrgPayChannelService orgPayChannelService;
    @Autowired
    private IOrgMerchantService orgMerchantService;
    @Autowired
    private IOrgChannelMerchantService orgChannelMerchantService;

    @RequiresPermissions("system:channelMerchant:view")
    @GetMapping()
    public String channelMerchant()
    {
        return prefix + "/channelMerchant";
    }

    /**
     * 查询商户通道列表
     */
    @RequiresPermissions("system:channelMerchant:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OrgChannelMerchant orgChannelMerchant)
    {
        startPage();
        List<OrgChannelMerchant> list = orgChannelMerchantService.selectOrgChannelMerchantList(orgChannelMerchant);
        return getDataTable(list);
    }

    /**
     * 导出商户通道列表
     */
    @RequiresPermissions("system:channelMerchant:export")
    @Log(title = "商户通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OrgChannelMerchant orgChannelMerchant)
    {
        List<OrgChannelMerchant> list = orgChannelMerchantService.selectOrgChannelMerchantList(orgChannelMerchant);
        ExcelUtil<OrgChannelMerchant> util = new ExcelUtil<OrgChannelMerchant>(OrgChannelMerchant.class);
        return util.exportExcel(list, "商户通道数据");
    }

    /**
     * 新增商户通道
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        OrgPayChannel payChannel= new OrgPayChannel();
        payChannel.setChannelStatus(1L);
        OrgMerchant merchant = new OrgMerchant();
        merchant.setMerchantStatus(1L);
        mmap.put("channels", orgPayChannelService.selectOrgPayChannelList(payChannel));
        mmap.put("merchants", orgMerchantService.selectOrgMerchantList(merchant));
        return prefix + "/add";
    }

    /**
     * 新增保存商户通道
     */
    @RequiresPermissions("system:channelMerchant:add")
    @Log(title = "商户通道", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OrgChannelMerchant orgChannelMerchant)
    {
        return toAjax(orgChannelMerchantService.insertOrgChannelMerchant(orgChannelMerchant));
    }

    /**
     * 修改商户通道
     */
    @RequiresPermissions("system:channelMerchant:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        OrgChannelMerchant orgChannelMerchant = orgChannelMerchantService.selectOrgChannelMerchantById(id);
        mmap.put("orgChannelMerchant", orgChannelMerchant);
        OrgPayChannel payChannel= new OrgPayChannel();
        payChannel.setChannelStatus(1L);
        OrgMerchant merchant = new OrgMerchant();
        merchant.setMerchantStatus(1L);
        mmap.put("channels", orgPayChannelService.selectOrgPayChannelList(payChannel));
        mmap.put("merchants", orgMerchantService.selectOrgMerchantList(merchant));
        return prefix + "/edit";
    }

    /**
     * 修改保存商户通道
     */
    @RequiresPermissions("system:channelMerchant:edit")
    @Log(title = "商户通道", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OrgChannelMerchant orgChannelMerchant)
    {
        return toAjax(orgChannelMerchantService.updateOrgChannelMerchant(orgChannelMerchant));
    }

    /**
     * 删除商户通道
     */
    @RequiresPermissions("system:channelMerchant:remove")
    @Log(title = "商户通道", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orgChannelMerchantService.deleteOrgChannelMerchantByIds(ids));
    }

    @RequiresPermissions("system:channelMerchant:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(OrgChannelMerchant orgChannelMerchant)
    {
        return toAjax(orgChannelMerchantService.updateOrgChannelMerchant(orgChannelMerchant));
    }


}
