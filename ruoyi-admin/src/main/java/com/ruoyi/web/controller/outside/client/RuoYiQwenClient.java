package com.ruoyi.web.controller.outside.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

//@Component
public class RuoYiQwenClient {
    private static final Logger log = LoggerFactory.getLogger(RuoYiQwenClient.class);

    @Value("${qwen.api.key:#{null}}")
    private String apiKey;

    @Value("${qwen.api.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}")
    private String baseUrl;

    @Value("${qwen.model:qwen-plus}")
    private String model;

    private NativeQwenClient nativeClient;

    @PostConstruct
    public void init() {
        if (apiKey != null && !apiKey.isEmpty()) {
            nativeClient = new NativeQwenClient(apiKey, baseUrl, model);
            log.info("千问客户端初始化成功，模型: {}", model);
        } else {
            log.warn("千问API Key未配置，相关功能不可用");
        }
    }

    /**
     * 调用千问API
     * @param prompt 输入提示
     * @return 响应内容
     */
    public String callQwen(String prompt) {
        if (nativeClient == null) {
            throw new IllegalStateException("千问客户端未初始化，请检查API Key配置");
        }

        try {
            NativeQwenClient.QwenResponse response = nativeClient.call(prompt);
            if (response.isSuccess()) {
                log.debug("千问API调用成功，输入: {}, 输出: {}", prompt, response.getContent());
                return response.getContent();
            } else {
                log.error("千问API调用失败，错误: {}", response.getErrorMsg());
                throw new RuntimeException("API调用失败: " + response.getErrorMsg());
            }
        } catch (IOException e) {
            log.error("千问API调用异常: {}", e.getMessage(), e);
            throw new RuntimeException("API调用异常: " + e.getMessage());
        }
    }

    public boolean isAvailable() {
        return nativeClient != null;
    }
}