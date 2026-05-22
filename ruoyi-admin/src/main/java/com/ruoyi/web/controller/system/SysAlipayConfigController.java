package com.ruoyi.system.controller;

import java.awt.*;
import java.io.File;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.ruoyi.common.utils.QRCodeUtil1;
import com.ruoyi.common.utils.security.Md5Utils;
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
import com.ruoyi.system.domain.SysAlipayConfig;
import com.ruoyi.system.service.ISysAlipayConfigService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * alipayConfigController
 *
 * @author ruoyi
 * @date 2023-06-10
 */
@Controller
@RequestMapping("/system/alipayConfig")
public class SysAlipayConfigController extends BaseController
{
    private String prefix = "system/alipayConfig";

    @Value(value = "${alipay.appUrl}")
    private String appUrl;
    @Value(value = "${alipay.apikey}")
    private String apikey;

    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;

    @RequiresPermissions("system:alipayConfig:view")
    @GetMapping()
    public String alipayConfig()
    {
        return prefix + "/alipayConfig";
    }

    /**
     * 查询alipayConfig列表
     */
    @RequiresPermissions("system:alipayConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysAlipayConfig sysAlipayConfig)
    {
        startPage();
        List<SysAlipayConfig> list = sysAlipayConfigService.selectSysAlipayConfigList(sysAlipayConfig);
        return getDataTable(list);
    }

    /**
     * 导出alipayConfig列表
     */
    @RequiresPermissions("system:alipayConfig:export")
    @Log(title = "alipayConfig", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysAlipayConfig sysAlipayConfig)
    {
        List<SysAlipayConfig> list = sysAlipayConfigService.selectSysAlipayConfigList(sysAlipayConfig);
        ExcelUtil<SysAlipayConfig> util = new ExcelUtil<SysAlipayConfig>(SysAlipayConfig.class);
        return util.exportExcel(list, "alipayConfig数据");
    }

    /**
     * 新增alipayConfig
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存alipayConfig
     */
    @RequiresPermissions("system:alipayConfig:add")
    @Log(title = "alipayConfig", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysAlipayConfig sysAlipayConfig)
    {
        return toAjax(sysAlipayConfigService.insertSysAlipayConfig(sysAlipayConfig));
    }

    /**
     * 修改alipayConfig
     */
    @RequiresPermissions("system:alipayConfig:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysAlipayConfig sysAlipayConfig = sysAlipayConfigService.selectSysAlipayConfigById(id);
        mmap.put("sysAlipayConfig", sysAlipayConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存alipayConfig
     */
    @RequiresPermissions("system:alipayConfig:edit")
    @Log(title = "alipayConfig", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysAlipayConfig sysAlipayConfig)
    {
        return toAjax(sysAlipayConfigService.updateSysAlipayConfig(sysAlipayConfig));
    }

    /**
     * 删除alipayConfig
     */
    @RequiresPermissions("system:alipayConfig:remove")
    @Log(title = "alipayConfig", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysAlipayConfigService.deleteSysAlipayConfigByIds(ids));
    }

    /**
     * 生成收款固定二维码图片 扫码后，跳转至对应地址
     */
    @RequiresPermissions("system:alipayConfig:getQRCode")
    @Log(title = "getQRCode", businessType = BusinessType.DELETE)
    @PostMapping( "/getQRCode")
    @ResponseBody
    public AjaxResult getQRCode(String id)
    {
        return toAjax(sysAlipayConfigService.deleteSysAlipayConfigByIds(id));
    }



    /**
     * 根据 url 生成 普通二维码
     */
    /**
     * 生成收款固定二维码图片 扫码后，跳转至对应地址
     */
    @RequiresPermissions("system:alipayConfig:createCommonQRCode")
    @Log(title = "createCommonQRCode", businessType = BusinessType.OTHER)
    @RequestMapping( "/createCommonQRCode/{id}")
    @ResponseBody
    public void createCommonQRCode(@PathVariable("id") Long id,HttpServletResponse response, HttpServletRequest request) throws Exception {
        ServletOutputStream stream = null;

        try {
            SysAlipayConfig sysAlipayConfig = sysAlipayConfigService.selectSysAlipayConfigById(id);
            String appid_id = sysAlipayConfig.getAPPID()+"___"+id+apikey;
            String Md5key = Md5Utils.hash(appid_id);
            stream = response.getOutputStream();
            QrConfig config = new QrConfig(600, 600);
//            // 设置边距，即二维码和背景之间的边距
//            config.setMargin(3);
//            // 设置前景色，即二维码颜色（青色）
//            config.setForeColor(Color.CYAN.getRGB());
//            // 设置背景色（灰色）
//            config.setBackColor(Color.GRAY.getRGB());
            // 生成二维码到文件，也可以到流
            QrCodeUtil.generate(appUrl+"/outside/pay/qrCode/alipay/P"+sysAlipayConfig.getAPPID()+Md5key, config,"jpg", stream);

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }

    }

    /**
     * 根据 url 生成 带有logo二维码
     */
    @RequiresPermissions("system:alipayConfig:createLogoQRCode")
    @Log(title = "createLogoQRCode", businessType = BusinessType.OTHER)
    @RequestMapping( "/createLogoQRCode/{id}")
    @ResponseBody
    public void createLogoQRCode(@PathVariable("id") Long id,HttpServletResponse response,HttpServletRequest request) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
//            String logoPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
//                    + "templates" + File.separator +"logo-"+UUID.randomUUID().toString().trim().replaceAll("-", "")+ ".jpg";
            String logoPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "templates" + File.separator +"logo.jpg";
            String url = appUrl;
            //使用工具类生成二维码
            QRCodeUtil1.encode(url, logoPath, stream, true);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }




    /**
     * 根据 url 生成 普通二维码
     */
    /**
     * 生成收款固定二维码图片 扫码后，跳转至对应地址
     */
//    @RequiresPermissions("system:alipayConfig:createCommonQRCode")
//    @Log(title = "createCommonQRCode", businessType = BusinessType.OTHER)
//    @RequestMapping( "/createCommonQRCode/{id}")
//    @ResponseBody
//    public void createCommonQRCode(@PathVariable("id") Long id,HttpServletResponse response, HttpServletRequest request) throws Exception {
//        ServletOutputStream stream = null;
//        try {
//            stream = response.getOutputStream();
//            String url = QRPayUrl;
//            //使用工具类生成二维码
//            QRCodeUtil1.encode(url, stream);
//        } catch (Exception e) {
//            e.getStackTrace();
//        } finally {
//            if (stream != null) {
//                stream.flush();
//                stream.close();
//            }
//        }
//    }
//    public static void main(String[] args) {
//       System.out.println(Md5Utils.hash("202100417061116995___595ad4df24fec408b590c10c4dc7fb827"));
//    }
}
