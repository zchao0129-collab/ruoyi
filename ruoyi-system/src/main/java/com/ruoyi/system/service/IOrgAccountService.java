package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OrgAccount;

/**
 * 客户Service接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface IOrgAccountService
{
    /**
     * 查询客户
     *
     * @param id 客户主键
     * @return 客户
     */
    public OrgAccount selectOrgAccountById(Long id);

    /**
     * 查询客户列表
     *
     * @param OrgAccount 客户
     * @return 客户集合
     */
    public List<OrgAccount> selectOrgAccountList(OrgAccount OrgAccount);

    /**
     * 新增客户
     *
     * @param OrgAccount 客户
     * @return 结果
     */
    public int insertOrgAccount(OrgAccount OrgAccount);

    /**
     * 修改客户
     *
     * @param OrgAccount 客户
     * @return 结果
     */
    public int updateOrgAccount(OrgAccount OrgAccount);

    /**
     * 批量删除客户
     *
     * @param ids 需要删除的客户主键集合
     * @return 结果
     */
    public int deleteOrgAccountByIds(String ids);

    /**
     * 删除客户信息
     *
     * @param id 客户主键
     * @return 结果
     */
    public int deleteOrgAccountById(Long id);


    public OrgAccount selectOne(OrgAccount OrgAccount);



}
