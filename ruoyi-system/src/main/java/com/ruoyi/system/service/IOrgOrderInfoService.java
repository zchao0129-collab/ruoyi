package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OrgOrderInfo;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface IOrgOrderInfoService
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
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteOrgOrderInfoByIds(String ids);



    public int seleteByIp(String ipadd);


    public int seleteByUid(String accountAppId,String uid);
    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    public int deleteOrgOrderInfoById(Long id);



     public List<OrgOrderInfo> selectPayorderStatusList(OrgOrderInfo orgOrderInfo);


    public String callbackOrder(OrgOrderInfo orgOrderInfo) ;

    public OrgOrderInfo selectorderByOrderId(String orderNo);

    public OrgOrderInfo queryOrder(OrgOrderInfo orderInfo);

    public void asyncThreadCQCallbackOrder(OrgOrderInfo orderInfo);

    boolean verifyPayment(String orderNo);
}
