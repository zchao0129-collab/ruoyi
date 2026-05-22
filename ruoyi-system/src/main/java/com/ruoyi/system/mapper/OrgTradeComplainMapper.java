package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OrgTradeComplain;

/**
 * tradecomplainMapper接口
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
public interface OrgTradeComplainMapper 
{
    /**
     * 查询tradecomplain
     * 
     * @param id tradecomplain主键
     * @return tradecomplain
     */
    public OrgTradeComplain selectOrgTradeComplainById(Long id);

    /**
     * 查询tradecomplain列表
     * 
     * @param orgTradeComplain tradecomplain
     * @return tradecomplain集合
     */
    public List<OrgTradeComplain> selectOrgTradeComplainList(OrgTradeComplain orgTradeComplain);

    /**
     * 新增tradecomplain
     * 
     * @param orgTradeComplain tradecomplain
     * @return 结果
     */
    public int insertOrgTradeComplain(OrgTradeComplain orgTradeComplain);

    /**
     * 修改tradecomplain
     * 
     * @param orgTradeComplain tradecomplain
     * @return 结果
     */
    public int updateOrgTradeComplain(OrgTradeComplain orgTradeComplain);

    /**
     * 删除tradecomplain
     * 
     * @param id tradecomplain主键
     * @return 结果
     */
    public int deleteOrgTradeComplainById(Long id);

    /**
     * 批量删除tradecomplain
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrgTradeComplainByIds(String[] ids);
}
