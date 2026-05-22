package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.service.IOrgAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrgOrderInfoMapper;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.service.IOrgOrderInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class OrgOrderInfoServiceImpl implements IOrgOrderInfoService
{
    @Autowired
    private OrgOrderInfoMapper orgOrderInfoMapper;

    @Autowired
    private IOrgAccountService orgAccountService;

    @Value(value = "${alipay.apikey}")
    private String apikey;

    private static final Logger log = LoggerFactory.getLogger(OrgOrderInfoServiceImpl.class);
    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public OrgOrderInfo selectOrgOrderInfoById(Long id)
    {
        return orgOrderInfoMapper.selectOrgOrderInfoById(id);
    }

    /**
     * 查询订单列表
     *
     * @param orgOrderInfo 订单
     * @return 订单
     */
    @Override
    public List<OrgOrderInfo> selectOrgOrderInfoList(OrgOrderInfo orgOrderInfo)
    {
        return orgOrderInfoMapper.selectOrgOrderInfoList(orgOrderInfo);
    }

    /**
     * 新增订单
     *
     * @param orgOrderInfo 订单
     * @return 结果
     */
    @Override
    public int insertOrgOrderInfo(OrgOrderInfo orgOrderInfo)
    {
        orgOrderInfo.setCreateTime(DateUtils.getNowDate());
        return orgOrderInfoMapper.insertOrgOrderInfo(orgOrderInfo);
    }

    /**
     * 修改订单
     *
     * @param orgOrderInfo 订单
     * @return 结果
     */
    @Override
    public int updateOrgOrderInfo(OrgOrderInfo orgOrderInfo)
    {
        return orgOrderInfoMapper.updateOrgOrderInfo(orgOrderInfo);
    }

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrgOrderInfoByIds(String ids)
    {
        return orgOrderInfoMapper.deleteOrgOrderInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    @Override
    public int deleteOrgOrderInfoById(Long id)
    {
        return orgOrderInfoMapper.deleteOrgOrderInfoById(id);
    }

    @Override
    public List<OrgOrderInfo> selectPayorderStatusList(OrgOrderInfo orgOrderInfo) {
        return orgOrderInfoMapper.selectPayorderStatusList(orgOrderInfo);
    }

    @Override
    public String callbackOrder(OrgOrderInfo order) {
        if(order.getOrderStatus() == 1 ){  //如果订单状态为0
            //如果已查询成功、但回调失败，需要重新回调！
            String result =  callbackUrl(order);
            return result;
        }else{
//            order = updateOrderPayStatus(order);
//            if(order.getStatus()==1){
//                String result =  callbackUrl(order);
//                return result;
//            }else{
            return "";
            // }
        }
    }

    private String callbackUrl(OrgOrderInfo orderInfo){
        try{
            if(StringUtils.isEmpty(orderInfo.getCallbackUrl())){
                return "";
            }
            OrgAccount account = orgAccountService.selectOrgAccountById(orderInfo.getAccountId());
            String parm = account.getAccountAppId()+orderInfo.getOrderNo()+orderInfo.getAccountOrderNo()+orderInfo.getOrderStatus()+ orderInfo.getAmount()+orderInfo.getCompletionTime().getTime();
            log.info("加密前未加token的串： "+parm);
            String sign = Md5Utils.hash(parm+account.getAccountToken()).toUpperCase();
            log.info("加token后的加密后的串： "+sign);
            String postData = "{\"appid\":\""+account.getAccountAppId()+"\"," +
                    "\"orderNo\":\""+orderInfo.getOrderNo()+"\"," +
                    "\"merchantOrderNo\":\""+orderInfo.getAccountOrderNo()+"\"," +
                    "\"payStatus\":\""+orderInfo.getOrderStatus()+"\"," +
                    "\"amount\":\""+orderInfo.getAmount()+"\"," +
                    "\"payTime\":\""+orderInfo.getCompletionTime().getTime()+"\"," +
                    "\"sign\":\""+sign+"\"}";
            log.info("回调参数： "+postData);
            log.info("回调地址： "+orderInfo.getCallbackUrl());
            String callbackJson = "";

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("appid", account.getAccountAppId());
            paramMap.put("orderNo", orderInfo.getOrderNo());
            paramMap.put("merchantOrderNo", orderInfo.getAccountOrderNo());
            paramMap.put("payStatus", orderInfo.getOrderStatus()+"");
            paramMap.put("amount", orderInfo.getAmount()+"");
            paramMap.put("payTime", orderInfo.getCompletionTime().getTime()+"");
            paramMap.put("sign", sign);
            callbackJson = HttpUtil.post(orderInfo.getCallbackUrl(), paramMap);
            log.info("------------回调返回值："+callbackJson);
            if("success".equals(callbackJson)){
                orderInfo.setCallbackStatus(1L);
                int count = orgOrderInfoMapper.updateOrgOrderInfo(orderInfo);
                if(count>0){
                    log.info("回调订单号："+orderInfo.getOrderNo()+"，成功");
                }
            }
            return callbackJson;
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return "";
    }

    @Override
    public OrgOrderInfo queryOrder(OrgOrderInfo orderInfo) {
        return orgOrderInfoMapper.queryOrder(orderInfo);
    }

    @Override
    public OrgOrderInfo selectorderByOrderId(String orderNo) {
        return orgOrderInfoMapper.selectorderByOrderId(orderNo);
    }

    @Override
    public void asyncThreadCQCallbackOrder(OrgOrderInfo orderInfo) {
        try{
            //BigDecimal game_amount= orderInfo.getAmount().multiply(new BigDecimal(15000));
            String parm = "game_amount="+orderInfo.getAmount()+"&game_user_id="+orderInfo.getAccountOrderNo()+"&account="+orderInfo.getAccountOrderNo()+"&money="+orderInfo.getAmount();
            log.info("加密前未加token的串： "+parm);
            String sign = Md5Utils.hash(parm+apikey);
            log.info("加token后的加密后的串： "+sign);
            log.info("回调地址： "+orderInfo.getCallbackUrl());
            String postData = "{\"game_amount\":\""+orderInfo.getAmount()+"\"," +
                    "\"game_user_id\":\""+orderInfo.getAccountOrderNo()+"\"," +
                    "\"account\":\""+orderInfo.getAccountOrderNo()+"\"," +
                    "\"money\":\""+orderInfo.getAmount()+"\"," +
                    "\"sign\":\""+sign+"\"}";
            log.info("回调参数： "+postData);
            String callbackJson = "";
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("game_amount", orderInfo.getAmount());
            paramMap.put("game_user_id", orderInfo.getAccountOrderNo());
            paramMap.put("account", orderInfo.getAccountOrderNo());
            paramMap.put("money", orderInfo.getAmount()+"");
            paramMap.put("sign", sign);
            callbackJson = HttpUtil.post(orderInfo.getCallbackUrl(), postData);
            log.info("------------回调返回值："+callbackJson);
            if("success".equals(callbackJson)){
                orderInfo.setCallbackStatus(1L);
                int count = orgOrderInfoMapper.updateOrgOrderInfo(orderInfo);
                if(count>0){
                    log.info("异步回调订单号："+orderInfo.getOrderNo()+"，成功");
                }
            }else{
                log.info("异步回调订单号："+orderInfo.getOrderNo()+"，失败！");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @Override
    public int seleteByIp(String ipadd) {
        return orgOrderInfoMapper.seleteByIp(ipadd);
    }

    @Override
    public int seleteByUid(String accountAppId,String uid) {
        return orgOrderInfoMapper.seleteByUid(accountAppId,uid);
    }

    @Override
    public boolean verifyPayment(String orderNo) {
        if(BeanUtil.isEmpty(orgOrderInfoMapper.seleteByOrderNo(orderNo))){
            return false;
        }else {
            return true;
        }
    }
}
