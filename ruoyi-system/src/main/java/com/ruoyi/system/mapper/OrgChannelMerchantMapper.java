package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OrgChannelMerchant;

/**
 * 商户通道Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface OrgChannelMerchantMapper
{
    /**
     * 查询商户通道
     *
     * @param id 商户通道主键
     * @return 商户通道
     */
    public OrgChannelMerchant selectOrgChannelMerchantById(Long id);

    /**
     * 查询商户通道列表
     *
     * @param orgChannelMerchant 商户通道
     * @return 商户通道集合
     */
    public List<OrgChannelMerchant> selectOrgChannelMerchantList(OrgChannelMerchant orgChannelMerchant);

    /**
     * 新增商户通道
     *
     * @param orgChannelMerchant 商户通道
     * @return 结果
     */
    public int insertOrgChannelMerchant(OrgChannelMerchant orgChannelMerchant);

    /**
     * 修改商户通道
     *
     * @param orgChannelMerchant 商户通道
     * @return 结果
     */
    public int updateOrgChannelMerchant(OrgChannelMerchant orgChannelMerchant);

    /**
     * 删除商户通道
     *
     * @param id 商户通道主键
     * @return 结果
     */
    public int deleteOrgChannelMerchantById(Long id);

    /**
     * 批量删除商户通道
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrgChannelMerchantByIds(String[] ids);

    OrgChannelMerchant selectByMerchantIdChannelId(OrgChannelMerchant ocm);

    OrgChannelMerchant selectCollectionRatioDesc();

    OrgChannelMerchant selectOrgChannelMerchantByMerchantNo(String merchantNo);
}
