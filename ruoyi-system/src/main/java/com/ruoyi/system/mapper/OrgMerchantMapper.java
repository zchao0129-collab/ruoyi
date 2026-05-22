package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OrgMerchant;

/**
 * 商户Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface OrgMerchantMapper
{
    /**
     * 查询商户
     *
     * @param id 商户主键
     * @return 商户
     */
    public OrgMerchant selectOrgMerchantById(Long id);

    /**
     * 查询商户列表
     *
     * @param orgMerchant 商户
     * @return 商户集合
     */
    public List<OrgMerchant> selectOrgMerchantList(OrgMerchant orgMerchant);

    /**
     * 新增商户
     *
     * @param orgMerchant 商户
     * @return 结果
     */
    public int insertOrgMerchant(OrgMerchant orgMerchant);

    /**
     * 修改商户
     *
     * @param orgMerchant 商户
     * @return 结果
     */
    public int updateOrgMerchant(OrgMerchant orgMerchant);

    /**
     * 删除商户
     *
     * @param id 商户主键
     * @return 结果
     */
    public int deleteOrgMerchantById(Long id);

    /**
     * 批量删除商户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrgMerchantByIds(String[] ids);


    public OrgMerchant selectOrgMerchantByMerchantId(Long merchantId);
}
