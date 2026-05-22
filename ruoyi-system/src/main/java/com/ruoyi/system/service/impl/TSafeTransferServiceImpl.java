package com.ruoyi.system.service.impl;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TSafeTransferMapper;
import com.ruoyi.system.domain.TSafeTransfer;
import com.ruoyi.system.service.ITSafeTransferService;
import com.ruoyi.common.core.text.Convert;

/**
 * 安全发Service业务层处理
 *
 * @author ruoyi
 * @date 2025-04-21
 */
@Service
public class TSafeTransferServiceImpl implements ITSafeTransferService
{
    @Autowired
    private TSafeTransferMapper tSafeTransferMapper;

    @Value(value = "${safeIssuedSetting.appId}")
    private String safeIssuedAppid;

    @Value(value = "${alipay.appUrl}")
    private String appUrl;
    @Value(value = "${safeIssuedSetting.appSecret}")
    private String safeIssuedappSecret;
    @Value(value = "${safeIssuedSetting.safeIssuedURL}")
    private String safeIssuedUrl;
    @Value(value = "${safeIssuedSetting.callbackIP}")
    private String safeIssuedIP;

    //转账到户
    private String transferUrl = "/openapi/alipay/transfer/account";
    //转账到卡
    private String transferToKaUrl = "/api/openapi/alipay/transfer/bankcard";
    //查詢訂單
    private String queryTransferUrl = "/openapi/alipay/transfer/getTransferDetail";
    //查詢余額
    private String queryBalance = "/openapi/account/info";



    private static final Logger logger = LoggerFactory.getLogger(TSafeTransferServiceImpl.class);

    /**
     * 查询安全发
     *
     * @param id 安全发主键
     * @return 安全发
     */
    @Override
    public TSafeTransfer selectTSafeTransferById(Long id)
    {
        return tSafeTransferMapper.selectTSafeTransferById(id);
    }




    /**
     * 查询安全发
     *
     * @param orderId 安全发主键
     * @return 安全发
     */
    @Override
    public TSafeTransfer selectTSafeTransferByOrderId(String orderId)
    {
        return tSafeTransferMapper.selectTSafeTransferByOrderId(orderId);
    }

    /**
     * 查询安全发列表
     *
     * @param tSafeTransfer 安全发
     * @return 安全发
     */
    @Override
    public List<TSafeTransfer> selectTSafeTransferList(TSafeTransfer tSafeTransfer)
    {
        return tSafeTransferMapper.selectTSafeTransferList(tSafeTransfer);
    }

    /**
     * 新增安全发
     *
     * @param tSafeTransfer 安全发
     * @return 结果
     */
    @Override
    public int insertTSafeTransfer(TSafeTransfer tSafeTransfer)
    {

        return tSafeTransferMapper.insertTSafeTransfer(tSafeTransfer);
    }

    /**
     * 修改安全发
     *
     * @param tSafeTransfer 安全发
     * @return 结果
     */
    @Override
    public int updateTSafeTransfer(TSafeTransfer tSafeTransfer)
    {
        return tSafeTransferMapper.updateTSafeTransfer(tSafeTransfer);
    }

    /**
     * 批量删除安全发
     *
     * @param ids 需要删除的安全发主键
     * @return 结果
     */
    @Override
    public int deleteTSafeTransferByIds(String ids)
    {
        return tSafeTransferMapper.deleteTSafeTransferByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除安全发信息
     *
     * @param id 安全发主键
     * @return 结果
     */
    @Override
    public int deleteTSafeTransferById(Long id)
    {
        return tSafeTransferMapper.deleteTSafeTransferById(id);
    }



    @Override
    public AjaxResult sendTSafeTransferByIds(Long id) {
        TSafeTransfer tSafeTransfer = tSafeTransferMapper.selectTSafeTransferById(id);
        JSONObject body = new JSONObject();
        body.put("accountId", tSafeTransfer.getAccountId());
        body.put("notifyUrl", appUrl+"/outside/notify/safeTransferCallback");
        JSONArray orderList = new JSONArray();
        JSONObject order = new JSONObject();
        order.put("channelMchId", tSafeTransfer.getOrderId());
        order.put("identity", tSafeTransfer.getPayeeInfoIdentity());
        order.put("transferAmount", tSafeTransfer.getTransAmount());
        order.put("recipientName", tSafeTransfer.getPayeeInfoName());
        order.put("alipayRemark", tSafeTransfer.getRemark());
        orderList.add(order);
        body.put("orderList", orderList);
        logger.info("body TO JSON！"+body.toJSONString());
        String res = "";
        if("ALIPAY".equals(tSafeTransfer.getTransferType())){
            res = post(transferUrl, body);
        }else {
            res = post(transferToKaUrl, body);
        }
        JSONObject json = JSONObject.parseObject(res);
        if("0".equals(json.getString("code"))){
            JSONObject jsonData = json.getJSONObject("data");
            String taskId = jsonData.getString("taskId");
            tSafeTransfer.setTaskId(taskId);
            tSafeTransferMapper.updateTSafeTransfer(tSafeTransfer);
            return new AjaxResult(AjaxResult.Type.SUCCESS,json.getString("msg"));
        }else{
            logger.info("下单失败！");
            return new AjaxResult(AjaxResult.Type.ERROR,"调用异常10010！");
        }
    }
    @Override
    public AjaxResult queryTransfer(Long id) {

        TSafeTransfer tSafeTransfer = tSafeTransferMapper.selectTSafeTransferById(id);

        JSONObject body = new JSONObject();
        JSONArray orderList = new JSONArray();
        orderList.add(tSafeTransfer.getOrderId());
        body.put("accessKey", safeIssuedAppid);
        body.put("timestamp", System.currentTimeMillis());
        body.put("channelMchIds", orderList);
        body.put("sign", getSign(body, safeIssuedappSecret));
        logger.info("请求参数 | " + body.toJSONString());
        String res = HttpUtil.post(safeIssuedUrl+queryTransferUrl, body.toJSONString());
        JSONObject json = JSONObject.parseObject(res);
        if("0".equals(json.getString("code"))){
            JSONArray jsonArray = json.getJSONArray("data");
            if(jsonArray.size()>0){
                JSONObject jsonData = JSONObject.parseObject(jsonArray.get(0).toString());
                String transferStatus = jsonData.getString("transferStatus");
                String description = jsonData.getString("description");
                logger.info("transferStatus : " + transferStatus);
                tSafeTransfer.setStatus(transferStatus);
                tSafeTransfer.setFailReason(description);
                tSafeTransferMapper.updateTSafeTransfer(tSafeTransfer);
                return new AjaxResult(AjaxResult.Type.SUCCESS,description);
            }
            return new AjaxResult(AjaxResult.Type.ERROR,"调用异常10010！");
        }else{
            logger.info("下单失败！");
            return new AjaxResult(AjaxResult.Type.ERROR,"调用异常10010！");
        }
    }

    @Override
    public AjaxResult queryBillReceipt(Long id) {
        return AjaxResult.success();
    }

    @Override
    public AjaxResult queryBalance() {
        JSONObject body = new JSONObject();
        body.put("accessKey", safeIssuedAppid);
        body.put("timestamp", System.currentTimeMillis());
        body.put("sign", getSign(body, safeIssuedappSecret));
        logger.info("请求参数 | " + body.toJSONString());
        String res = HttpUtil.post(safeIssuedUrl+queryBalance, body.toJSONString());
        JSONObject json = JSONObject.parseObject(res);
        if("0".equals(json.getString("code"))){
            JSONObject jsonData = json.getJSONObject("data");
            String balance = jsonData.getString("balance");
            return new AjaxResult(AjaxResult.Type.SUCCESS,balance);
        }else{
            logger.info("下单失败！");
            return new AjaxResult(AjaxResult.Type.ERROR,"调用异常10010！");
        }
    }


    public static String getSignStr(JSONObject body, String secret) {
        body.remove("sign");
        // 将请求参数按照参数名ASCII码从小到大排序
        List<String> sortedKeys = new ArrayList<>(body.keySet());
        Collections.sort(sortedKeys);

        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys) {
            String value = String.valueOf(body.get(key));
            if (StringUtils.isNotBlank(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb + "&secret=" + secret;
    }

    public String getSign(JSONObject body, String secret) {
        String signStr = getSignStr(body, secret);
        return DigestUtils.md5Hex(signStr).toUpperCase();
    }

    public String post(String api, JSONObject body) {
        String url = safeIssuedUrl + api;
        body.put("accessKey", safeIssuedAppid);
        body.put("timestamp", System.currentTimeMillis());
        body.put("sign", getSign(body, safeIssuedappSecret));
        logger.info("请求参数 | " + body.toJSONString());
        String res = HttpUtil.post(url, body.toJSONString());
        logger.info("响应参数 | " + res);
        return res;
    }
}
