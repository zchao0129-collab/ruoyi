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
import com.ruoyi.system.domain.OrgTradeComplain;
import com.ruoyi.system.service.IOrgTradeComplainService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * tradecomplainController
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
@Controller
@RequestMapping("/system/complain")
public class OrgTradeComplainController extends BaseController
{
    private String prefix = "system/complain";

    @Autowired
    private IOrgTradeComplainService orgTradeComplainService;

    @RequiresPermissions("system:complain:view")
    @GetMapping()
    public String complain()
    {
        return prefix + "/complain";
    }

    /**
     * 查询tradecomplain列表
     */
    @RequiresPermissions("system:complain:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OrgTradeComplain orgTradeComplain)
    {
        startPage();
        List<OrgTradeComplain> list = orgTradeComplainService.selectOrgTradeComplainList(orgTradeComplain);
        return getDataTable(list);
    }

    /**
     * 导出tradecomplain列表
     */
    @RequiresPermissions("system:complain:export")
    @Log(title = "tradecomplain", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OrgTradeComplain orgTradeComplain)
    {
        List<OrgTradeComplain> list = orgTradeComplainService.selectOrgTradeComplainList(orgTradeComplain);
        ExcelUtil<OrgTradeComplain> util = new ExcelUtil<OrgTradeComplain>(OrgTradeComplain.class);
        return util.exportExcel(list, "tradecomplain数据");
    }

    /**
     * 新增tradecomplain
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存tradecomplain
     */
    @RequiresPermissions("system:complain:add")
    @Log(title = "tradecomplain", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OrgTradeComplain orgTradeComplain)
    {
        return toAjax(orgTradeComplainService.insertOrgTradeComplain(orgTradeComplain));
    }

    /**
     * 修改tradecomplain
     */
    @RequiresPermissions("system:complain:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        OrgTradeComplain orgTradeComplain = orgTradeComplainService.selectOrgTradeComplainById(id);
        mmap.put("orgTradeComplain", orgTradeComplain);
        return prefix + "/edit";
    }

    /**
     * 修改保存tradecomplain
     */
    @RequiresPermissions("system:complain:edit")
    @Log(title = "tradecomplain", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OrgTradeComplain orgTradeComplain)
    {
        return toAjax(orgTradeComplainService.updateOrgTradeComplain(orgTradeComplain));
    }

    /**
     * 删除tradecomplain
     */
    @RequiresPermissions("system:complain:remove")
    @Log(title = "tradecomplain", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orgTradeComplainService.deleteOrgTradeComplainByIds(ids));
    }
}
