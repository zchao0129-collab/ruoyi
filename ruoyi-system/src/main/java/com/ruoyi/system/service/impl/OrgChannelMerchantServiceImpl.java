package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgChannelMerchantMapper;
import com.ruoyi.system.domain.OrgChannelMerchant;
import com.ruoyi.system.service.IOrgChannelMerchantService;
import com.ruoyi.common.core.text.Convert;

/**
 * 商户通道Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class OrgChannelMerchantServiceImpl implements IOrgChannelMerchantService
{
    @Autowired
    private OrgChannelMerchantMapper orgChannelMerchantMapper;

    /**
     * 查询商户通道
     *
     * @param id 商户通道主键
     * @return 商户通道
     */
    @Override
    public OrgChannelMerchant selectOrgChannelMerchantById(Long id)
    {
        return orgChannelMerchantMapper.selectOrgChannelMerchantById(id);
    }

    /**
     * 查询商户通道列表
     *
     * @param orgChannelMerchant 商户通道
     * @return 商户通道
     */
    @Override
    public List<OrgChannelMerchant> selectOrgChannelMerchantList(OrgChannelMerchant orgChannelMerchant)
    {
        return orgChannelMerchantMapper.selectOrgChannelMerchantList(orgChannelMerchant);
    }

    /**
     * 新增商户通道
     *
     * @param orgChannelMerchant 商户通道
     * @return 结果
     */
    @Override
    public int insertOrgChannelMerchant(OrgChannelMerchant orgChannelMerchant)
    {
        return orgChannelMerchantMapper.insertOrgChannelMerchant(orgChannelMerchant);
    }

    /**
     * 修改商户通道
     *
     * @param orgChannelMerchant 商户通道
     * @return 结果
     */
    @Override
    public int updateOrgChannelMerchant(OrgChannelMerchant orgChannelMerchant)
    {
        return orgChannelMerchantMapper.updateOrgChannelMerchant(orgChannelMerchant);
    }

    /**
     * 批量删除商户通道
     *
     * @param ids 需要删除的商户通道主键
     * @return 结果
     */
    @Override
    public int deleteOrgChannelMerchantByIds(String ids)
    {
        return orgChannelMerchantMapper.deleteOrgChannelMerchantByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商户通道信息
     *
     * @param id 商户通道主键
     * @return 结果
     */
    @Override
    public int deleteOrgChannelMerchantById(Long id)
    {
        return orgChannelMerchantMapper.deleteOrgChannelMerchantById(id);
    }

    @Override
    public OrgChannelMerchant selectByMerchantIdChannelId(Long merchantId, Long channelId) {
        OrgChannelMerchant ocm = new OrgChannelMerchant();
        ocm.setMerchantId(merchantId);
        ocm.setChannelId(channelId);
        return orgChannelMerchantMapper.selectByMerchantIdChannelId(ocm);
    }

    @Override
    public OrgChannelMerchant selectCollectionRatioDesc() {
        return orgChannelMerchantMapper.selectCollectionRatioDesc();
    }

    @Override
    public OrgChannelMerchant selectOrgChannelMerchantByMerchantNo(String merchantNo) {
        return orgChannelMerchantMapper.selectOrgChannelMerchantByMerchantNo(merchantNo);
    }
}
