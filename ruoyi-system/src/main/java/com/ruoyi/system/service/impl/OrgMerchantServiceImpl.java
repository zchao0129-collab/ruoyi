package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgMerchantMapper;
import com.ruoyi.system.domain.OrgMerchant;
import com.ruoyi.system.service.IOrgMerchantService;
import com.ruoyi.common.core.text.Convert;

/**
 * 商户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class OrgMerchantServiceImpl implements IOrgMerchantService
{
    @Autowired
    private OrgMerchantMapper orgMerchantMapper;

    /**
     * 查询商户
     *
     * @param id 商户主键
     * @return 商户
     */
    @Override
    public OrgMerchant selectOrgMerchantById(Long id)
    {
        return orgMerchantMapper.selectOrgMerchantById(id);
    }

    /**
     * 查询商户列表
     *
     * @param orgMerchant 商户
     * @return 商户
     */
    @Override
    public List<OrgMerchant> selectOrgMerchantList(OrgMerchant orgMerchant)
    {
        return orgMerchantMapper.selectOrgMerchantList(orgMerchant);
    }

    /**
     * 新增商户
     *
     * @param orgMerchant 商户
     * @return 结果
     */
    @Override
    public int insertOrgMerchant(OrgMerchant orgMerchant)
    {
        return orgMerchantMapper.insertOrgMerchant(orgMerchant);
    }

    /**
     * 修改商户
     *
     * @param orgMerchant 商户
     * @return 结果
     */
    @Override
    public int updateOrgMerchant(OrgMerchant orgMerchant)
    {
        return orgMerchantMapper.updateOrgMerchant(orgMerchant);
    }

    /**
     * 批量删除商户
     *
     * @param ids 需要删除的商户主键
     * @return 结果
     */
    @Override
    public int deleteOrgMerchantByIds(String ids)
    {
        return orgMerchantMapper.deleteOrgMerchantByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商户信息
     *
     * @param id 商户主键
     * @return 结果
     */
    @Override
    public int deleteOrgMerchantById(Long id)
    {
        return orgMerchantMapper.deleteOrgMerchantById(id);
    }

    @Override
    public OrgMerchant selectOrgMerchantByMerchantId(Long merchantId) {
        return orgMerchantMapper.selectOrgMerchantByMerchantId(merchantId);
    }
}
