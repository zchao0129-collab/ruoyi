package com.ruoyi.web.controller.outside.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年03月16日 12:13
 */
public class OutsideQwenVO extends BaseEntity {
    private String question;
    private String orderNo;
    private String ask;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "OutsideQwenVO{" +
                "question='" + question + '\'' +
                "orderNo='" + orderNo + '\'' +
                ", ask='" + ask + '\'' +
                '}';
    }
}
