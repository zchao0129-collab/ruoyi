package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OrgPayChannel;

/**
 * 通道Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-08
 */
public interface OrgPayChannelMapper 
{
    /**
     * 查询通道
     * 
     * @param id 通道主键
     * @return 通道
     */
    public OrgPayChannel selectOrgPayChannelById(Long id);

    /**
     * 查询通道列表
     * 
     * @param orgPayChannel 通道
     * @return 通道集合
     */
    public List<OrgPayChannel> selectOrgPayChannelList(OrgPayChannel orgPayChannel);

    /**
     * 新增通道
     * 
     * @param orgPayChannel 通道
     * @return 结果
     */
    public int insertOrgPayChannel(OrgPayChannel orgPayChannel);

    /**
     * 修改通道
     * 
     * @param orgPayChannel 通道
     * @return 结果
     */
    public int updateOrgPayChannel(OrgPayChannel orgPayChannel);

    /**
     * 删除通道
     * 
     * @param id 通道主键
     * @return 结果
     */
    public int deleteOrgPayChannelById(Long id);

    /**
     * 批量删除通道
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrgPayChannelByIds(String[] ids);
}
