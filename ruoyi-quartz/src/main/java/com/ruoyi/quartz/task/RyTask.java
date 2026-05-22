package com.ruoyi.quartz.task;

import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.domain.OrgTradeComplain;
import com.ruoyi.system.service.AlipayServer;
import com.ruoyi.system.service.IOrgOrderInfoService;
import com.ruoyi.system.service.IOrgTradeComplainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{

    @Autowired
    IOrgOrderInfoService orderInfoService;

    @Autowired
    AlipayServer alipayServer;

    @Autowired
    private IOrgTradeComplainService tradeComplainService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        logger.info("订单回调定时任务开始");
        OrgOrderInfo orgOrderInfo = new OrgOrderInfo();
        orgOrderInfo.setCallbackStatus(0L);
        List<OrgOrderInfo> orgOrderInfoList = orderInfoService.selectPayorderStatusList(orgOrderInfo);
        if(orgOrderInfoList != null&& orgOrderInfoList.size()>0){
            for (OrgOrderInfo order:orgOrderInfoList) {
                orderInfoService.callbackOrder(order);
            }
        }
        logger.info("订单回调定时任务结束");
    }


    public void synctradecomplain() throws Exception {
        logger.info("处理投诉定时任务开始");
        OrgTradeComplain orgTradeComplain = new OrgTradeComplain();
        orgTradeComplain.setStatus("MERCHANT_PROCESSING");
        List<OrgTradeComplain> orgTradeComplainList = tradeComplainService.selectOrgTradeComplainList(orgTradeComplain);
        if(orgTradeComplainList != null&& orgTradeComplainList.size()>0){
            for (OrgTradeComplain tradeComplain:orgTradeComplainList) {
                alipayServer.synctradecomplain(tradeComplain);
            }
        }
        logger.info("处理投诉定时任务结束");
    }



    public void syncTradecomplainBatchquery() throws Exception {
        logger.info("查询投诉列表开始");
        alipayServer.alipayMerchantTradecomplainBatchquery();
        logger.info("查询投诉列表结束");
    }
}
