package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgPayChannelMapper;
import com.ruoyi.system.domain.OrgPayChannel;
import com.ruoyi.system.service.IOrgPayChannelService;
import com.ruoyi.common.core.text.Convert;

/**
 * 通道Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class OrgPayChannelServiceImpl implements IOrgPayChannelService
{
    @Autowired
    private OrgPayChannelMapper orgPayChannelMapper;

    /**
     * 查询通道
     *
     * @param id 通道主键
     * @return 通道
     */
    @Override
    public OrgPayChannel selectOrgPayChannelById(Long id)
    {
        return orgPayChannelMapper.selectOrgPayChannelById(id);
    }

    /**
     * 查询通道列表
     *
     * @param orgPayChannel 通道
     * @return 通道
     */
    @Override
    public List<OrgPayChannel> selectOrgPayChannelList(OrgPayChannel orgPayChannel)
    {
        return orgPayChannelMapper.selectOrgPayChannelList(orgPayChannel);
    }

    /**
     * 新增通道
     *
     * @param orgPayChannel 通道
     * @return 结果
     */
    @Override
    public int insertOrgPayChannel(OrgPayChannel orgPayChannel)
    {
        return orgPayChannelMapper.insertOrgPayChannel(orgPayChannel);
    }

    /**
     * 修改通道
     *
     * @param orgPayChannel 通道
     * @return 结果
     */
    @Override
    public int updateOrgPayChannel(OrgPayChannel orgPayChannel)
    {
        return orgPayChannelMapper.updateOrgPayChannel(orgPayChannel);
    }

    /**
     * 批量删除通道
     *
     * @param ids 需要删除的通道主键
     * @return 结果
     */
    @Override
    public int deleteOrgPayChannelByIds(String ids)
    {
        return orgPayChannelMapper.deleteOrgPayChannelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通道信息
     *
     * @param id 通道主键
     * @return 结果
     */
    @Override
    public int deleteOrgPayChannelById(Long id)
    {
        return orgPayChannelMapper.deleteOrgPayChannelById(id);
    }

}
