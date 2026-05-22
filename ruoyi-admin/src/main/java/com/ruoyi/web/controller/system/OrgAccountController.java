package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWTUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.service.IOrgAccountService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.apache.shiro.SecurityUtils;
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
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户Controller
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Controller
@RequestMapping("/system/account")
public class OrgAccountController extends BaseController
{
    private String prefix = "system/account";

    @Autowired
    private IOrgAccountService orgAccountService;

    @Autowired
    private ISysUserService sysUserService;


    @RequiresPermissions("system:account:view")
    @GetMapping()
    public String account()
    {
        return prefix + "/account";
    }

    /**
     * 查询客户列表
     */
    @RequiresPermissions("system:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OrgAccount orgAccount)
    {
        startPage();
        SysUser currentUser = ShiroUtils.getSysUser();
        String loginName = currentUser.getLoginName();
        logger.info("loginName:"+loginName);
        if(!"admin".equals(loginName) && !"admin123".equals(loginName)){
            orgAccount.setAccountName(loginName);
        }
        List<OrgAccount> list = orgAccountService.selectOrgAccountList(orgAccount);
        return getDataTable(list);
    }

    /**
     * 导出客户列表
     */
    @RequiresPermissions("system:account:export")
    @Log(title = "客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OrgAccount orgAccount)
    {
        List<OrgAccount> list = orgAccountService.selectOrgAccountList(orgAccount);
        ExcelUtil<OrgAccount> util = new ExcelUtil<OrgAccount>(OrgAccount.class);
        return util.exportExcel(list, "客户数据");
    }

    /**
     * 新增客户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> currentUser = sysUserService.selectUserList(new SysUser());
        currentUser.removeIf(next -> "admin".equals(next.getLoginName()) || "admin123".equals(next.getLoginName())|| "ry".equals(next.getLoginName()));
        mmap.put("sysUsers", currentUser);
        return prefix + "/add";
    }

    /**
     * 新增保存客户
     */
    @RequiresPermissions("system:account:add")
    @Log(title = "客户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OrgAccount orgAccount)
    {
        if(orgAccount.getId() == null ){
            orgAccount.setAccountAppId(IdUtil.fastSimpleUUID());
            String token = getToken(orgAccount.getAccountAppId());
            orgAccount.setAccountToken(token);
            orgAccount.setCreatedTime(new Date());
        }
        return toAjax(orgAccountService.insertOrgAccount(orgAccount));
    }


    /**
     *  成生token
     */
    @RequiresPermissions("system:account:getToken")
    @GetMapping("/getToken/{appId}")
    @ResponseBody
    public String createToken(@PathVariable("appId") String appId)
    {
        return createToken(appId);
    }

    public String getToken(String appid){
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("uid", Integer.parseInt("888"));
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            }
        };
        return JWTUtil.createToken(map, appid.getBytes());
    }
    /**
     * 修改客户
     */
    @RequiresPermissions("system:account:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        OrgAccount orgAccount = orgAccountService.selectOrgAccountById(id);
        mmap.put("orgAccount", orgAccount);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户
     */
    @RequiresPermissions("system:account:edit")
    @Log(title = "客户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(OrgAccount orgAccount)
    {
        return toAjax(orgAccountService.updateOrgAccount(orgAccount));
    }

    /**
     * 删除客户
     */
    @RequiresPermissions("system:account:remove")
    @Log(title = "客户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(orgAccountService.deleteOrgAccountByIds(ids));
    }



    @RequiresPermissions("system:account:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(OrgAccount orgAccount)
    {
        return toAjax(orgAccountService.updateOrgAccount(orgAccount));
    }


}
