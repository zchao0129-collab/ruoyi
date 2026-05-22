package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OrgOrderInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 订单Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface OrgOrderInfoMapper
{
    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    public OrgOrderInfo selectOrgOrderInfoById(Long id);

    /**
     * 查询订单列表
     *
     * @param orgOrderInfo 订单
     * @return 订单集合
     */
    public List<OrgOrderInfo> selectOrgOrderInfoList(OrgOrderInfo orgOrderInfo);

    /**
     * 新增订单
     *
     * @param orgOrderInfo 订单
     * @return 结果
     */
    public int insertOrgOrderInfo(OrgOrderInfo orgOrderInfo);

    /**
     * 修改订单
     *
     * @param orgOrderInfo 订单
     * @return 结果
     */
    public int updateOrgOrderInfo(OrgOrderInfo orgOrderInfo);

    /**
     * 删除订单
     *
     * @param id 订单主键
     * @return 结果
     */
    public int deleteOrgOrderInfoById(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrgOrderInfoByIds(String[] ids);

    List<OrgOrderInfo> selectPayorderStatusList(OrgOrderInfo orgOrderInfo);


    public OrgOrderInfo queryOrder(OrgOrderInfo orgOrderInfo);

    OrgOrderInfo selectorderByOrderId(String orderNo);

    public int seleteByIp(String ipadd);

    public int seleteByUid(@Param("accountAppId")String accountAppId, @Param("uid")String uid);

    OrgOrderInfo seleteByOrderNo(String orderNo);
}
