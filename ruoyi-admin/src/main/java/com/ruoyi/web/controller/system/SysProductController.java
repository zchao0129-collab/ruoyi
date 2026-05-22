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
import com.ruoyi.system.domain.SysProduct;
import com.ruoyi.system.service.ISysProductService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品Controller
 *
 * @author ruoyi
 * @date 2023-04-12
 */
@Controller
@RequestMapping("/system/product")
public class SysProductController extends BaseController
{
    private String prefix = "system/product";

    @Autowired
    private ISysProductService sysProductService;

    @RequiresPermissions("system:product:view")
    @GetMapping()
    public String product()
    {
        return prefix + "/product";
    }

    /**
     * 查询商品列表
     */
    @RequiresPermissions("system:product:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysProduct sysProduct)
    {
        startPage();
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        return getDataTable(list);
    }

    /**
     * 导出商品列表
     */
    @RequiresPermissions("system:product:export")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysProduct sysProduct)
    {
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        ExcelUtil<SysProduct> util = new ExcelUtil<SysProduct>(SysProduct.class);
        return util.exportExcel(list, "商品数据");
    }

    /**
     * 新增商品
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商品
     */
    @RequiresPermissions("system:product:add")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysProduct sysProduct)
    {
        return toAjax(sysProductService.insertSysProduct(sysProduct));
    }

    /**
     * 修改商品
     */
    @RequiresPermissions("system:product:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysProduct sysProduct = sysProductService.selectSysProductById(id);
        mmap.put("sysProduct", sysProduct);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品
     */
    @RequiresPermissions("system:product:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysProduct sysProduct)
    {
        return toAjax(sysProductService.updateSysProduct(sysProduct));
    }

    /**
     * 删除商品
     */
    @RequiresPermissions("system:product:remove")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysProductService.deleteSysProductByIds(ids));
    }
}
