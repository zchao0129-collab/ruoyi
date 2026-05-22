package com.ruoyi.system.service.impl;

import java.util.List;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alipay.api.domain.QrcodeEntity;
import com.ruoyi.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAlipayConfigMapper;
import com.ruoyi.system.domain.SysAlipayConfig;
import com.ruoyi.system.service.ISysAlipayConfigService;
import com.ruoyi.common.core.text.Convert;

/**
 * alipayConfigService业务层处理
 *
 * @author ruoyi
 * @date 2023-06-10
 */
@Service
public class SysAlipayConfigServiceImpl implements ISysAlipayConfigService
{

    @Value(value = "${alipay.orderPay}")
    private String orderPay;


    @Autowired
    private SysAlipayConfigMapper sysAlipayConfigMapper;

    /**
     * 查询alipayConfig
     *
     * @param id alipayConfig主键
     * @return alipayConfig
     */
    @Override
    public SysAlipayConfig selectSysAlipayConfigById(Long id)
    {
        return sysAlipayConfigMapper.selectSysAlipayConfigById(id);
    }


    @Override
    public SysAlipayConfig selectSysAlipayConfigStatusTopOne()
    {
        return sysAlipayConfigMapper.selectSysAlipayConfigStatusTopOne();
    }

    @Override
    public SysAlipayConfig selectSysAlipayConfigStatusWeight(int weight)
    {
        return sysAlipayConfigMapper.selectSysAlipayConfigStatusWeight(weight);
    }
    @Override
    public SysAlipayConfig selectSysAlipayConfig(String appId)
    {
        return sysAlipayConfigMapper.selectSysAlipayConfig(appId);
    }


    /**
     * 查询alipayConfig列表
     *
     * @param sysAlipayConfig alipayConfig
     * @return alipayConfig
     */
    @Override
    public List<SysAlipayConfig> selectSysAlipayConfigList(SysAlipayConfig sysAlipayConfig)
    {
        return sysAlipayConfigMapper.selectSysAlipayConfigList(sysAlipayConfig);
    }

    /**
     * 新增alipayConfig
     *
     * @param sysAlipayConfig alipayConfig
     * @return 结果
     */
    @Override
    public int insertSysAlipayConfig(SysAlipayConfig sysAlipayConfig)
    {
        return sysAlipayConfigMapper.insertSysAlipayConfig(sysAlipayConfig);
    }

    /**
     * 修改alipayConfig
     *
     * @param sysAlipayConfig alipayConfig
     * @return 结果
     */
    @Override
    public int updateSysAlipayConfig(SysAlipayConfig sysAlipayConfig)
    {
        return sysAlipayConfigMapper.updateSysAlipayConfig(sysAlipayConfig);
    }

    /**
     * 批量删除alipayConfig
     *
     * @param ids 需要删除的alipayConfig主键
     * @return 结果
     */
    @Override
    public int deleteSysAlipayConfigByIds(String ids)
    {
        return sysAlipayConfigMapper.deleteSysAlipayConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除alipayConfig信息
     *
     * @param id alipayConfig主键
     * @return 结果
     */
    @Override
    public int deleteSysAlipayConfigById(Long id)
    {
        return sysAlipayConfigMapper.deleteSysAlipayConfigById(id);
    }


}
