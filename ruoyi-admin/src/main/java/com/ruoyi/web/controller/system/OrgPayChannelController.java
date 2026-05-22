package com.ruoyi.web.controller.system;

import java.util.List;

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
import com.ruoyi.system.domain.OrgPayChannel;
import com.ruoyi.system.service.IOrgPayChannelService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通道Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Controller
@RequestMapping("/system/channel")
public class OrgPayChannelController extends BaseController
{
    private String prefix = "system/channel";

    @Autowired
    private IOrgPayChannelService orgPayChannelService;

    @RequiresPermissions("system:channel:view")
    @GetMapping()
    public String channel()
    {
        return prefix + "/channel";
    }

    /**
     * 查询通道列表
     */
    @RequiresPermissions("system:channel:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OrgPayChannel orgPayChannel)
    {
        startPage();
        List<OrgPayChannel> list = orgPayChannelService.selectOrgPayChannelList(orgPayChannel);
        return getDataTable(list);
    }

    /**
     * 导出通道列表
     */
    @RequiresPermissions("system:channel:export")
    @Log(title = "通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OrgPayChannel orgPayChannel)
    {
        List<OrgPayChannel> list = orgPayChannelService.selectOrgPayChannelList(orgPayChannel);
        ExcelUtil<OrgPayChannel> util = new ExcelUtil<OrgPayChannel>(OrgPayChannel.class);
        return util.exportExcel(list, "通道数据");
    }

    /**
     * 新增通道
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存通道
     */
    @RequiresPermissions("system:channel:add")
    @Log(title = "通道", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OrgPayChannel orgPayChannel)
    {
        return toAjax(orgPayChannelService.insertOrgPayChannel(orgPayChannel));
    }

    /**
     * 修改通道
     */
    @RequiresPermissions("system:channel:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        OrgPayChannel orgPayChannel = orgPayChannelService.selectOrgPayChannelById(id);
        mmap.put("orgPayChannel", orgPayChannel);
        return prefix + "/edit";
    }

    /**
     * 修改保存通道
     */
    @RequiresPermissions("system:channel:edit")
    @Log(title = "通道", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OrgPayChannel orgPayChannel)
    {
        return toAjax(orgPayChannelService.updateOrgPayChannel(orgPayChannel));
    }

    /**
     * 删除通道
     */
    @RequiresPermissions("system:channel:remove")
    @Log(title = "通道", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orgPayChannelService.deleteOrgPayChannelByIds(ids));
    }


    @RequiresPermissions("system:channel:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(OrgPayChannel orgPayChannel)
    {
        return toAjax(orgPayChannelService.updateOrgPayChannel(orgPayChannel));
    }
}
