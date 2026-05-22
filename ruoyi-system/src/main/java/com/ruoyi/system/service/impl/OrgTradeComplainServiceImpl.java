package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgTradeComplainMapper;
import com.ruoyi.system.domain.OrgTradeComplain;
import com.ruoyi.system.service.IOrgTradeComplainService;
import com.ruoyi.common.core.text.Convert;

/**
 * tradecomplainService业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
@Service
public class OrgTradeComplainServiceImpl implements IOrgTradeComplainService 
{
    @Autowired
    private OrgTradeComplainMapper orgTradeComplainMapper;

    /**
     * 查询tradecomplain
     * 
     * @param id tradecomplain主键
     * @return tradecomplain
     */
    @Override
    public OrgTradeComplain selectOrgTradeComplainById(Long id)
    {
        return orgTradeComplainMapper.selectOrgTradeComplainById(id);
    }

    /**
     * 查询tradecomplain列表
     * 
     * @param orgTradeComplain tradecomplain
     * @return tradecomplain
     */
    @Override
    public List<OrgTradeComplain> selectOrgTradeComplainList(OrgTradeComplain orgTradeComplain)
    {
        return orgTradeComplainMapper.selectOrgTradeComplainList(orgTradeComplain);
    }

    /**
     * 新增tradecomplain
     * 
     * @param orgTradeComplain tradecomplain
     * @return 结果
     */
    @Override
    public int insertOrgTradeComplain(OrgTradeComplain orgTradeComplain)
    {
        return orgTradeComplainMapper.insertOrgTradeComplain(orgTradeComplain);
    }

    /**
     * 修改tradecomplain
     * 
     * @param orgTradeComplain tradecomplain
     * @return 结果
     */
    @Override
    public int updateOrgTradeComplain(OrgTradeComplain orgTradeComplain)
    {
        return orgTradeComplainMapper.updateOrgTradeComplain(orgTradeComplain);
    }

    /**
     * 批量删除tradecomplain
     * 
     * @param ids 需要删除的tradecomplain主键
     * @return 结果
     */
    @Override
    public int deleteOrgTradeComplainByIds(String ids)
    {
        return orgTradeComplainMapper.deleteOrgTradeComplainByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除tradecomplain信息
     * 
     * @param id tradecomplain主键
     * @return 结果
     */
    @Override
    public int deleteOrgTradeComplainById(Long id)
    {
        return orgTradeComplainMapper.deleteOrgTradeComplainById(id);
    }
}
