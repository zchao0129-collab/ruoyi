package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysAlipayConfig;

/**
 * alipayConfigService接口
 *
 * @author ruoyi
 * @date 2023-06-10
 */
public interface ISysAlipayConfigService
{
    /**
     * 查询alipayConfig
     *
     * @param id alipayConfig主键
     * @return alipayConfig
     */
    public SysAlipayConfig selectSysAlipayConfigById(Long id);

    public SysAlipayConfig selectSysAlipayConfigStatusTopOne();

    public SysAlipayConfig selectSysAlipayConfigStatusWeight(int weight);

    public SysAlipayConfig  selectSysAlipayConfig(String appId);

    /**
     * 查询alipayConfig列表
     *
     * @param sysAlipayConfig alipayConfig
     * @return alipayConfig集合
     */
    public List<SysAlipayConfig> selectSysAlipayConfigList(SysAlipayConfig sysAlipayConfig);


    /**
     * 新增alipayConfig
     *
     * @param sysAlipayConfig alipayConfig
     * @return 结果
     */
    public int insertSysAlipayConfig(SysAlipayConfig sysAlipayConfig);

    /**
     * 修改alipayConfig
     *
     * @param sysAlipayConfig alipayConfig
     * @return 结果
     */
    public int updateSysAlipayConfig(SysAlipayConfig sysAlipayConfig);

    /**
     * 批量删除alipayConfig
     *
     * @param ids 需要删除的alipayConfig主键集合
     * @return 结果
     */
    public int deleteSysAlipayConfigByIds(String ids);

    /**
     * 删除alipayConfig信息
     *
     * @param id alipayConfig主键
     * @return 结果
     */
    public int deleteSysAlipayConfigById(Long id);



}
