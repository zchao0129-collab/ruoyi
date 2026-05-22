package com.ruoyi.system.service.impl;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.QwenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Configuration
public class QwenServiceImpl implements QwenService {

    @Value("${qwen.api.key}")
    private String apiKey;

    @Value("${qwen.api.url}")
    private String apiUrl;

    private static final String DEFAULT_API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_DEFAULT = "qwen-plus";



    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AjaxResult getShortAnswer(String question) {
        try {
            // 调用千问API获取简要回答
            Map<String, Object> requestBody = buildRequestPayload(question, 50); // 限制返回50个字符作为简要回答
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String shortAnswer = extractContent(responseBody, 50); // 提取前50个字符作为简要回答
                return new AjaxResult(AjaxResult.Type.SUCCESS, "获取详细回答成功", shortAnswer);
            } else {
                return new AjaxResult(AjaxResult.Type.ERROR, "API调用失败", null);
            }
        } catch (Exception e) {
            return new AjaxResult(AjaxResult.Type.ERROR, "服务异常：" + e.getMessage(), null);
        }

    }

    @Override
    public AjaxResult getDetailedAnswer(String question) {
        try {
            // 调用千问API获取详细回答
            Map<String, Object> requestBody = buildRequestPayload(question, 0); // 不限制返回长度
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String detailedAnswer = extractContent(responseBody, 0); // 返回完整回答
                return new AjaxResult(AjaxResult.Type.SUCCESS, "获取详细回答成功", detailedAnswer);
            } else {
                return new AjaxResult(AjaxResult.Type.ERROR, "API调用失败", null);
            }
        } catch (Exception e) {
            return new AjaxResult(AjaxResult.Type.ERROR, "服务异常：" + e.getMessage(), null);
        }
    }

    private Map<String, Object> buildRequestPayload(String question, int maxTokens) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "qwen-turbo"); // 使用千问模型

        // 创建消息数组，包含用户的问题
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", question);
        messages.add(userMessage);

        payload.put("messages", messages);

        if (maxTokens > 0) {
            payload.put("max_tokens", maxTokens);
        }

        return payload;
    }

    private String extractContent(Map<String, Object> response, int maxLength) {
        // 这里简化处理，实际应根据千问API返回的结构提取内容
        if (response.containsKey("choices")) {
            Object choicesObj = response.get("choices");
            if (choicesObj instanceof List) {
                List<?> choicesList = (List<?>) choicesObj;
                if (!choicesList.isEmpty()) {
                    Object firstChoice = choicesList.get(0);
                    if (firstChoice instanceof Map) {
                        Map<?, ?> choiceMap = (Map<?, ?>) firstChoice;
                        if (choiceMap.containsKey("message")) {
                            Map<?, ?> messageMap = (Map<?, ?>) choiceMap.get("message");
                            String content = (String) messageMap.get("content");

                            if (maxLength > 0 && content.length() > maxLength) {
                                return content.substring(0, maxLength) + "...";
                            }
                            return content;
                        }
                    }
                }
            }
        }
        return "无法获取回答内容";
    }

    @Override
    public AjaxResult getAnswer(String prompt) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 设置请求方法和头部信息
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "NativeQwenClient/1.0");

        // 启用输入输出流
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // 构建请求体
        String requestBody = buildRequestBody(prompt);

        // 发送请求体
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 获取响应码
        int responseCode = conn.getResponseCode();

        // 读取响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream(),
                        StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        // 解析响应
        if (responseCode >= 200 && responseCode < 300) {
            return new AjaxResult(AjaxResult.Type.SUCCESS, "获取回答成功", response.toString());
        } else {
            return new AjaxResult(AjaxResult.Type.ERROR, "API调用失败", parseErrorMessage(response.toString()));
        }
    }



    /**
     * 构建API请求体
     * @param prompt 输入的提示文本
     * @return JSON格式的请求体字符串
     */
    private String buildRequestBody(String prompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"model\": \"").append(escapeJson(MODEL_DEFAULT)).append("\",\n");
        sb.append("  \"messages\": [\n");
        sb.append("    {\n");
        sb.append("      \"role\": \"user\",\n");
        sb.append("      \"content\": \"").append(escapeJson(prompt)).append("\"\n");
        sb.append("    }\n");
        sb.append("  ],\n");
        sb.append("  \"stream\": false,\n");
        sb.append("  \"temperature\": 0.7\n");
        sb.append("}");
        return sb.toString();
    }


    /**
     * 转义JSON字符串中的特殊字符
     * @param input 需要转义的字符串
     * @return 转义后的字符串
     */
    private String escapeJson(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * 反转义JSON字符串
     * @param input 需要反转义的字符串
     * @return 反转义后的字符串
     */
    private String unescapeJson(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    /**
     * 解析错误信息
     * @param jsonResponse 错误响应
     * @return 错误消息
     */
    private String parseErrorMessage(String jsonResponse) {
        // 尝试提取错误信息
        Pattern messagePattern = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]*)\"");
        Matcher messageMatcher = messagePattern.matcher(jsonResponse);

        if (messageMatcher.find()) {
            return unescapeJson(messageMatcher.group(1));
        }

        // 尝试提取error下的message
        Pattern errorPattern = Pattern.compile("\"error\"\\s*:\\s*\\{[^}]*\"message\"\\s*:\\s*\"([^\"]*)\"");
        Matcher errorMatcher = errorPattern.matcher(jsonResponse);

        if (errorMatcher.find()) {
            return unescapeJson(errorMatcher.group(1));
        }

        return "API调用失败: " + jsonResponse;
    }

}
