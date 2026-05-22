package com.ruoyi.web.controller.outside.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用Java原生HTTP调用千问大模型
 * 无需任何第三方依赖
 */
public class NativeQwenClient {

    private static final String DEFAULT_API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String MODEL_DEFAULT = "qwen-plus";

    private final String apiKey;
    private final String baseUrl;
    private final String model;

    public NativeQwenClient(String apiKey) {
        this(apiKey, DEFAULT_API_URL, MODEL_DEFAULT);
    }

    public NativeQwenClient(String apiKey, String baseUrl) {
        this(apiKey, baseUrl, MODEL_DEFAULT);
    }

    public NativeQwenClient(String apiKey, String baseUrl, String model) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API Key不能为空");
        }
        this.apiKey = apiKey;
        this.baseUrl = baseUrl != null ? baseUrl : DEFAULT_API_URL;
        this.model = model != null ? model : MODEL_DEFAULT;
    }

    /**
     * 同步调用千问API
     * @param prompt 输入的提示文本
     * @return API响应结果
     * @throws IOException 网络异常
     */
    public QwenResponse call(String prompt) throws IOException {
        URL url = new URL(baseUrl);
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
        QwenResponse result = new QwenResponse();
        result.setStatusCode(responseCode);
        result.setRawResponse(response.toString());

        if (responseCode >= 200 && responseCode < 300) {
            result.setSuccess(true);
            result.setContent(extractContent(response.toString()));
        } else {
            result.setSuccess(false);
            result.setErrorMsg(parseErrorMessage(response.toString()));
        }

        return result;
    }

    /**
     * 构建API请求体
     * @param prompt 输入的提示文本
     * @return JSON格式的请求体字符串
     */
    private String buildRequestBody(String prompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"model\": \"").append(escapeJson(model)).append("\",\n");
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
     * 提取响应中的文本内容
     * @param jsonResponse 完整的JSON响应
     * @return 提取的文本内容
     */
    private String extractContent(String jsonResponse) {
        // 使用正则表达式提取content字段的值
        Pattern pattern = Pattern.compile("\"content\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        // 如果找不到content字段，尝试其他可能的字段名
        pattern = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]*)\"");
        matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        return "无法从响应中提取内容: " + jsonResponse;
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
     * 响应结果类
     */
    public static class QwenResponse {
        private boolean success;
        private String content;
        private String errorMsg;
        private int statusCode;
        private String rawResponse;

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getErrorMsg() { return errorMsg; }
        public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }

        public int getStatusCode() { return statusCode; }
        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

        public String getRawResponse() { return rawResponse; }
        public void setRawResponse(String rawResponse) { this.rawResponse = rawResponse; }

        @Override
        public String toString() {
            if (success) {
                return "QwenResponse{success=true, content='" + content + "'}";
            } else {
                return "QwenResponse{success=false, statusCode=" + statusCode + ", errorMsg='" + errorMsg + "'}";
            }
        }
    }

    // 测试主方法
    public static void main(String[] args) {
        // 从环境变量获取API Key
        String apiKey = "sk-ef85be9eceb44b87ab04d63263c7df50";
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("请设置环境变量 DASHSCOPE_API_KEY");
            return;
        }

        NativeQwenClient client = new NativeQwenClient(apiKey);

        try {
            System.out.println("正在调用千问API...");
            QwenResponse response = client.call("你能做一些什麽事？ ");

            if (response.isSuccess()) {
                System.out.println("调用成功！");
                System.out.println("内容: " + response.getContent());
            } else {
                System.out.println("调用失败！");
                System.out.println("状态码: " + response.getStatusCode());
                System.out.println("错误: " + response.getErrorMsg());
            }
        } catch (IOException e) {
            System.err.println("网络错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


