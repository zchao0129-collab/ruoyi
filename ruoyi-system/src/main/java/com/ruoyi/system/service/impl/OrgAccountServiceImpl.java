package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgAccountMapper;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.service.IOrgAccountService;
import com.ruoyi.common.core.text.Convert;

/**
 * 客户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class OrgAccountServiceImpl implements IOrgAccountService
{
    @Autowired
    private OrgAccountMapper orgAccountMapper;

    /**
     * 查询客户
     *
     * @param id 客户主键
     * @return 客户
     */
    @Override
    public OrgAccount selectOrgAccountById(Long id)
    {
        return orgAccountMapper.selectOrgAccountById(id);
    }

    /**
     * 查询客户列表
     *
     * @param OrgAccount 客户
     * @return 客户
     */
    @Override
    public List<OrgAccount> selectOrgAccountList(OrgAccount OrgAccount)
    {
        return orgAccountMapper.selectOrgAccountList(OrgAccount);
    }

    /**
     * 新增客户
     *
     * @param OrgAccount 客户
     * @return 结果
     */
    @Override
    public int insertOrgAccount(OrgAccount OrgAccount)
    {
        return orgAccountMapper.insertOrgAccount(OrgAccount);
    }

    /**
     * 修改客户
     *
     * @param OrgAccount 客户
     * @return 结果
     */
    @Override
    public int updateOrgAccount(OrgAccount OrgAccount)
    {
        return orgAccountMapper.updateOrgAccount(OrgAccount);
    }

    /**
     * 批量删除客户
     *
     * @param ids 需要删除的客户主键
     * @return 结果
     */
    @Override
    public int deleteOrgAccountByIds(String ids)
    {
        return orgAccountMapper.deleteOrgAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户信息
     *
     * @param id 客户主键
     * @return 结果
     */
    @Override
    public int deleteOrgAccountById(Long id)
    {
        return orgAccountMapper.deleteOrgAccountById(id);
    }

    @Override
    public OrgAccount selectOne(OrgAccount OrgAccount) {
        return orgAccountMapper.selectOne(OrgAccount);
    }

}
