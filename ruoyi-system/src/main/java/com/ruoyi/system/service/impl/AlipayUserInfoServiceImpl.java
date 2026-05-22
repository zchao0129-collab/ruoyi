package com.ruoyi.system.service.impl;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.uwqkejicn.Hzxj_demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AlipayUserInfoMapper;
import com.ruoyi.system.domain.AlipayUserInfo;
import com.ruoyi.system.service.IAlipayUserInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 支付宝用户信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-19
 */
@Service
public class AlipayUserInfoServiceImpl implements IAlipayUserInfoService
{

    @Value(value = "${blacklist.blackNameValue}")
    private String blackKey;

    @Value(value = "${blacklist.blackNameListURL}")
    private String blackUrl;


    @Autowired
    private AlipayUserInfoMapper alipayUserInfoMapper;



    private static final Logger logger = LoggerFactory.getLogger(AlipayUserInfoServiceImpl.class);

    /**
     * 查询支付宝用户信息
     *
     * @param id 支付宝用户信息主键
     * @return 支付宝用户信息
     */
    @Override
    public AlipayUserInfo selectAlipayUserInfoById(Long id)
    {
        return alipayUserInfoMapper.selectAlipayUserInfoById(id);
    }

    /**
     * 查询支付宝用户信息列表
     *
     * @param alipayUserInfo 支付宝用户信息
     * @return 支付宝用户信息
     */
    @Override
    public List<AlipayUserInfo> selectAlipayUserInfoList(AlipayUserInfo alipayUserInfo)
    {
        return alipayUserInfoMapper.selectAlipayUserInfoList(alipayUserInfo);
    }

    /**
     * 新增支付宝用户信息
     *
     * @param alipayUserInfo 支付宝用户信息
     * @return 结果
     */
    @Override
    public int insertAlipayUserInfo(AlipayUserInfo alipayUserInfo)
    {
        return alipayUserInfoMapper.insertAlipayUserInfo(alipayUserInfo);
    }

    /**
     * 修改支付宝用户信息
     *
     * @param alipayUserInfo 支付宝用户信息
     * @return 结果
     */
    @Override
    public int updateAlipayUserInfo(AlipayUserInfo alipayUserInfo)
    {
        return alipayUserInfoMapper.updateAlipayUserInfo(alipayUserInfo);
    }

    /**
     * 批量删除支付宝用户信息
     *
     * @param ids 需要删除的支付宝用户信息主键
     * @return 结果
     */
    @Override
    public int deleteAlipayUserInfoByIds(String ids)
    {
        return alipayUserInfoMapper.deleteAlipayUserInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付宝用户信息信息
     *
     * @param id 支付宝用户信息主键
     * @return 结果
     */
    @Override
    public int deleteAlipayUserInfoById(Long id)
    {
        return alipayUserInfoMapper.deleteAlipayUserInfoById(id);
    }

    @Override
    public int countByAppid(String appid) {
        return alipayUserInfoMapper.countByAppid(appid);
    }

    @Override
    public int countByAppidAndUid(AlipayUserInfo alipayUserInfo) {
        return alipayUserInfoMapper.countByAppidAndUid(alipayUserInfo);
    }

    @Override
    public AlipayUserInfo selectAlipayUserInfoByUid(String uid) {
        return alipayUserInfoMapper.selectAlipayUserInfoByUid(uid);
    }

    @Override
    public AlipayUserInfo selectAlipayUserInfoByAppidAndUid(AlipayUserInfo alipayUserInfo) {
        return alipayUserInfoMapper.selectAlipayUserInfoByAppidAndUid(alipayUserInfo);
    }


    @Override
    public boolean useridIsBlackList(String userId) {
        logger.info("-------------------useridIsBlackList!------------------");
        String aftSignString = "id="+userId+"&key="+blackKey;
        String  sign = Md5Utils.hash(aftSignString);
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("id",userId);  //商户号
        map.put("sign", sign);  //订单号 上送订单号唯一,
        String callbackJson = null;
        try {
            callbackJson = Hzxj_demo.sendPost(blackUrl, Hzxj_demo.mapToStringAndTrim(map),"utf-8");
            logger.info("-------------------useridIsBlackList!----------callbackJson-------:"+callbackJson);
            JSONObject json = JSONObject.parseObject(callbackJson);
            if("no".equals(json.getString("idIsBlacklist"))){
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            logger.info("-------------------useridIsBlackList!----------IOException-------:"+e.getMessage());
            throw new RuntimeException(e);
        }finally {
            return true;
        }
    }


}
