package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TSafeTransfer;

/**
 * 安全发Service接口
 *
 * @author ruoyi
 * @date 2025-04-21
 */
public interface ITSafeTransferService
{
    /**
     * 查询安全发
     *
     * @param id 安全发主键
     * @return 安全发
     */
    public TSafeTransfer selectTSafeTransferById(Long id);


    /**
     * 查询安全发
     *
     * @param orderId 安全发主键
     * @return 安全发
     */
    public TSafeTransfer selectTSafeTransferByOrderId(String orderId);



        /**
         * 查询安全发列表
         *
         * @param tSafeTransfer 安全发
         * @return 安全发集合
         */
    public List<TSafeTransfer> selectTSafeTransferList(TSafeTransfer tSafeTransfer);

    /**
     * 新增安全发
     *
     * @param tSafeTransfer 安全发
     * @return 结果
     */
    public int insertTSafeTransfer(TSafeTransfer tSafeTransfer);

    /**
     * 修改安全发
     *
     * @param tSafeTransfer 安全发
     * @return 结果
     */
    public int updateTSafeTransfer(TSafeTransfer tSafeTransfer);

    /**
     * 批量删除安全发
     *
     * @param ids 需要删除的安全发主键集合
     * @return 结果
     */
    public int deleteTSafeTransferByIds(String ids);

    /**
     * 删除安全发信息
     *
     * @param id 安全发主键
     * @return 结果
     */
    public int deleteTSafeTransferById(Long id);


    /**
     * 转账下单
     *
     * @param id 安全发主键
     * @return 结果
     */
    public AjaxResult sendTSafeTransferByIds(Long id);

    /**
     * 查询订单
     *
     * @param id 安全发主键
     * @return 结果
     */
    public AjaxResult queryTransfer(Long id);


    /**
     * 查询转账凭证
     *
     * @param id 安全发主键
     * @return 结果
     */
    public AjaxResult queryBillReceipt(Long id);


    /**
     * 查询余额
     *
     * @return 结果
     */
    public AjaxResult queryBalance();
}
