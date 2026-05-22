package com.ruoyi.common.utils.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * httprequest
 * 秋枫：2020.5.6
 */
public class HttpRequest {
    //    网址
    private String url;
    //    类型[POST,GET]
    private String mode;
    //    协议头
    private HashMap<String, String> headers;
    //    cookie
    private String cookies;


    private String location;
    //    提交数据
    private HashMap<String, String> submitdata;

    //    返回数据
    private String data;
//    Post(String url, String param, HashMap<String, String> headers, String cookie) {

    public HttpRequest(String url, String mode) {
        /**
         * 只有url和访问类型
         */
        this.url = url;
        this.mode = mode;
        if (mode.equals("POST")) {
            this.Post(this.url, null, null, null);
        } else if (mode.equals("GET")) {
            this.sendGet(this.url, null, null, null);
        }

    }

    public HttpRequest(String url, String mode, HashMap<String, String> submitdata) {
        /**
         * url
         * 访问类型
         * 提交数据
         */
        this.url = url;
        this.mode = mode;
        this.submitdata = submitdata;
        if (mode.equals("POST")) {
            this.Post(this.url, this.Processingdata(this.submitdata), null, null);
        } else if (mode.equals("GET")) {
            this.sendGet(this.url, this.Processingdata(this.submitdata), null, null);
        }
    }

    public HttpRequest(String url, String mode, HashMap<String, String> headers, HashMap<String, String> submitdata) {

        /**
         * url
         * 提交类型
         * 协议头
         * 提交数据
         *
         */
        this.url = url;
        this.mode = mode;
        this.headers = headers;
        this.submitdata = submitdata;
        if (mode.equals("POST")) {
            this.Post(this.url, this.Processingdata(this.submitdata), this.headers, null);

        } else if (mode.equals("GET")) {
            this.sendGet(this.url, this.Processingdata(this.submitdata), this.headers, null);
        }
    }

    public HttpRequest(String url, String mode, String cookies, HashMap<String, String> submitdata) {
        /**
         * url
         * 访问类型
         * cookie
         * 提交数据
         */
        this.url = url;
        this.mode = mode;
        this.cookies = cookies;
        this.submitdata = submitdata;
        if (mode.equals("POST")) {
            this.Post(this.url, this.Processingdata(this.submitdata), null, this.cookies);

        } else if (mode.equals("GET")) {
            this.sendGet(this.url, this.Processingdata(this.submitdata), null, this.cookies);
        }
    }

    public HttpRequest(String url, String mode, HashMap<String, String> headers, String cookies, HashMap<String, String> submitdata) {

        /**
         * url
         * 访问类型
         * 协议头
         * cookie
         * 提交数据
         */
        this.url = url;
        this.mode = mode;
        this.headers = headers;
        this.cookies = cookies;
        this.submitdata = submitdata;
        if (mode.equals("POST")) {
            this.Post(this.url, this.Processingdata(this.submitdata), this.headers, this.cookies);

        } else if (mode.equals("GET")) {
            this.sendGet(this.url, this.Processingdata(this.submitdata), this.headers, this.cookies);
        }


    }

    private String Processingdata(HashMap<String, String> submitdata) {

        String retu = "";

        for (Map.Entry<String, String> entry : submitdata.entrySet()) {
            retu = retu + "&" + entry.getKey() + "=" + entry.getValue();

        }
        return retu;
    }

    public String getCookies() {
        return this.cookies;
    }


    /**
     * 向指定URL发送GET方式的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     **/
    private void sendGet(String url, String param, HashMap<String, String> headers, String cookie) {
        StringBuilder result = new StringBuilder();
        String urlName = url + "?" + param;

        try {
            URL realUrl = new URL(urlName);


            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }

            //建立实际的连接
            conn.connect();
            //获取所有的响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            //遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "-->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //取cookie
            StringBuilder sessionId = new StringBuilder();
            String key = null;
            for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                System.out.println(key + "-->" );
                if (key.equalsIgnoreCase("set-cookie")) {
                    sessionId.append(conn.getHeaderField(i)).append(";");
                }
            }
            this.cookies = sessionId.toString();


            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常" + e);
            e.printStackTrace();
        }
        this.data = result.toString();
    }


    /**
     * 向指定URL发送POST方式的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数
     */
    private void Post(String url, String param, HashMap<String, String> headers, String cookie) {

        /**
         * 参数HashMap<String,String> headers说明：
         * HashMap<String,String> headers =new HashMap<String, String>();
         * headers.put("键", "键值");
         */

        StringBuilder result = new StringBuilder();
        String line;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    conn.addRequestProperty(k, v);
                }
            }


            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }

            //发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            //取cookie
            String sessionId = "";
            String cookieVal = "";
            String location = "";
            String key = null;
            for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("set-cookie")) {
                    cookieVal = conn.getHeaderField(i);
                    sessionId = sessionId + cookieVal + ";";
                }
                if (key.equalsIgnoreCase("location")) {
                    location = conn.getHeaderField(i);
                }
            }
            this.cookies = sessionId;
            this.location = location;

            while ((line = in.readLine()) != null) {
                result.append("\n").append(line);
            }


        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }

        this.data = result.toString();
//        System.out.println(this.data);

    }

    public String getData() {
        return this.data;
    }


    //测试发送GET和POST请求
    public static void main(String[] args) throws Exception {


    }

    public static void test1(){
        //发送POST请求
        //协议头
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-mssvc-sec-ts", "1587820160");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("X-mssvc-access-id", "HQQ7P0SxOla0n7yg");
        headers.put("X-app-machine", "iPhone 7");
        headers.put("X-app-system-version", "13.3");
        headers.put("X-device-code", "C88A0C48-C02C-401D-BDD3-BD1775F78D9D");
        headers.put("Accept", "application/json");
        // headers.put("Cookie","userName_cc=dd_itm4hbep; clientId=70d1f116-1ede-41d4-92f2-7091a357e280; loginToken=ac9fc61e-a372-4a2b-a4a5-1daf0edf64df; refreshToken=601b9900-daff-46d4-8262-3f4f4cecc294; login.dd373.com=85248d0b-c1e1-42f0-9fa0-659a0db1f5f7; newpay.dd373.com=90ec3de0-5987-4495-932c-8cb19431ced3; goods.dd373.com=1d088add-bea1-4c21-bd83-401f6e009805; point.dd373.com=a6ece75d-05bb-4718-a481-275b349f2584; newuser.dd373.com=5e9556bd-e15a-4459-8775-eb83c48936e5; mission.dd373.com=bec05890-3fec-4429-982a-e9da1760024f; thirdbind.dd373.com=8d5e30fb-0419-402a-8492-99a551c61a06; imservice.dd373.com=1245034d-3ec8-45c0-aae7-1eb447016563");
//        //请求数据
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("userName", "");
//        data.put("userPwd", "");
//        data.put("verifyCode", "");
//
//        HttpRequest post_text = new HttpRequest("https://网址", "POST", data);
//        System.out.println(post_text.getData()); //获取请求返回数据
//        System.out.println(post_text.getCookies()); //获取返回的cookie


        HashMap<String,String> map = new HashMap<String,String>();
        map.put("method","alipay.trade.wap.com.ruoyi.xinsheng.pay");
        map.put("app_id","2021002190664476");
        map.put("timestamp","2022-12-10 13:01:59");
        map.put("format","json");
        map.put("version","1.0");
        map.put("alipay_sdk","alipay-easysdk-php-2.0.0");
        map.put("charset","UTF-8");
        map.put("sign_type","RSA2");
        map.put("biz_content","{\"subject\":\"CRMEB--米家 小米电动牙刷T302成人\\/学生 4种净齿....\",\"out_trade_no\":\"cp332868853002403840\",\"total_amount\":\"0.01\",\"quit_url\":\"\",\"product_code\":\"QUICK_WAP_WAY\",\"passback_params\":\"product\"}");
        map.put("notify_url","http://h5.mall2.yingliao.tv/api/pay/notify/alipay.html");
        map.put("sign","Bi4uTx924GpYa82Zh8CIbFJ3mCpoNcDC0zJnwdQAdpCRjty7yI2gxuylXBikUBhq9flCq3AWoSarcbLnE8bDzklf5bUUCuvTZb9Lr4eb3giYTrrVuqOyxeS9hYMmGleozL1TSaeBD/k6SiWI3FTaAH8FeSTshm5nVeEQiS3XVx4KS/Xp1AQlbW4ZJOrPZUZYMZBFiTjvipeajBC8OOy7ww73kaNa2ju7Kt3/soPQopmjtSD8bOguVs06Uzj5gsv+jA3emE1ndGkDxiEd9uLPX9yVFPv1DWyS+TcPvvGgh928EL1/vvKLDc+wX8PztiiUguoW+zaZivT0Wkg1bHDCgQ==");

        //String cookie = "userName_cc=dd_itm4hbep; clientId=70d1f116-1ede-41d4-92f2-7091a357e280; refreshToken=601b9900-daff-46d4-8262-3f4f4cecc294; loginToken=2ba66f10-7893-4e51-a2ff-07e22de5665b; newpay.dd373.com=b69117f2-9076-4ac1-a298-d04374f8c1d1; acw_tc=76b20fe916697417249981735e4f19e07f03b95afa13fbb2d7bdafd2b157d5;SERVERID=a278729d2ae086497be277567757b907|1669743724|1669741725;";
        String cookie = "newCookie:uoken=2ba66f10-7374f8c1d1; ;SERVERID=892afa290ce7bed84795cafc25e9c37c|1669745974|1669745974";
        HttpRequest gettext = new HttpRequest("https://openapi.alipay.com/gateway.do?charset=UTF-8", "POST",headers,cookie,map);
        StringBuffer sb = new StringBuffer();
        String newCookie = "";
        String requestCookie = gettext.getCookies();
        String[] newCookies = requestCookie.split(";");
        for (int i=0;i<newCookies.length;i++) {
            if(newCookies[i].indexOf("SERVERID=")>-1){
                newCookie = newCookies[i];
            }
        }
        System.out.println("newCookie："+newCookie);
        if(cookie.indexOf("SERVERID=")!=-1){
            String[] cookies = cookie.split(";");
            for (int i=0;i<cookies.length;i++) {
                if(cookies[i].indexOf("SERVERID=")==-1){
                    sb.append(cookies[i]+";");
                }else{
                    sb.append(newCookie+";");
                }
            }
        }else{
            sb.append(cookie+";");
            sb.append(newCookie);
        }
        System.out.println(gettext.getData());  //获取返回数据
        System.out.println(gettext.getCookies());  //返回cookie
        System.out.println("oldCookie:"+cookie);  //返回cookie
        System.out.println("newCookie:"+sb.toString());  //返回cookie
    }


    public static void test2() throws IOException {
     String url = "https://openapi.alipay.com/gateway.do?charset=UTF-8";
//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("method","alipay.trade.wap.com.ruoyi.xinsheng.pay");
//        map.put("app_id","2021002190664476");
//        map.put("timestamp","2022-12-10 13:01:59");
//        map.put("format","json");
//        map.put("version","1.0");
//        map.put("alipay_sdk","alipay-easysdk-php-2.0.0");
//        map.put("charset","UTF-8");
//        map.put("sign_type","RSA2");
//        map.put("biz_content","{\"subject\":\"CRMEB--米家 小米电动牙刷T302成人\\/学生 4种净齿....\",\"out_trade_no\":\"cp332868853002403840\",\"total_amount\":\"0.01\",\"quit_url\":\"\",\"product_code\":\"QUICK_WAP_WAY\",\"passback_params\":\"product\"}");
//        map.put("notify_url","http://h5.mall2.yingliao.tv/api/pay/notify/alipay.html");
//        map.put("sign","Bi4uTx924GpYa82Zh8CIbFJ3mCpoNcDC0zJnwdQAdpCRjty7yI2gxuylXBikUBhq9flCq3AWoSarcbLnE8bDzklf5bUUCuvTZb9Lr4eb3giYTrrVuqOyxeS9hYMmGleozL1TSaeBD/k6SiWI3FTaAH8FeSTshm5nVeEQiS3XVx4KS/Xp1AQlbW4ZJOrPZUZYMZBFiTjvipeajBC8OOy7ww73kaNa2ju7Kt3/soPQopmjtSD8bOguVs06Uzj5gsv+jA3emE1ndGkDxiEd9uLPX9yVFPv1DWyS+TcPvvGgh928EL1/vvKLDc+wX8PztiiUguoW+zaZivT0Wkg1bHDCgQ==");
//        String alipayUrlresultStr = HttpUtils.sendPost(url,JSONObject.toJSONString(map),null);
//        JSONObject alipayUrlresultObj  = JSONObject.parseObject(alipayUrlresultStr);


        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "------------------------------0ea3fcae38ff", Charset.defaultCharset());
        multipartEntity.addPart("method", new StringBody("alipay.trade.wap.com.ruoyi.xinsheng.pay", Charset.forName("UTF-8")));
        multipartEntity.addPart("app_id", new StringBody("2021002190664476", Charset.forName("UTF-8")));
        multipartEntity.addPart("timestamp", new StringBody("2022-12-10 13:01:59", Charset.forName("UTF-8")));
        multipartEntity.addPart("format", new StringBody("json", Charset.forName("UTF-8")));
        multipartEntity.addPart("version", new StringBody("1.0", Charset.forName("UTF-8")));
        multipartEntity.addPart("alipay_sdk", new StringBody("alipay-easysdk-php-2.0.0", Charset.forName("UTF-8")));
        multipartEntity.addPart("charset", new StringBody("UTF-8", Charset.forName("UTF-8")));
        multipartEntity.addPart("sign_type", new StringBody("RSA2", Charset.forName("UTF-8")));
        multipartEntity.addPart("biz_content", new StringBody("{\"subject\":\"CRMEB--米家 小米电动牙刷T302成人\\/学生 4种净齿....\",\"out_trade_no\":\"cp332868853002403840\",\"total_amount\":\"0.01\",\"quit_url\":\"\",\"product_code\":\"QUICK_WAP_WAY\",\"passback_params\":\"product\"}", Charset.forName("UTF-8")));
        multipartEntity.addPart("notify_url", new StringBody("http://h5.mall2.yingliao.tv/api/pay/notify/alipay.html", Charset.forName("UTF-8")));
        multipartEntity.addPart("sign", new StringBody("Bi4uTx924GpYa82Zh8CIbFJ3mCpoNcDC0zJnwdQAdpCRjty7yI2gxuylXBikUBhq9flCq3AWoSarcbLnE8bDzklf5bUUCuvTZb9Lr4eb3giYTrrVuqOyxeS9hYMmGleozL1TSaeBD/k6SiWI3FTaAH8FeSTshm5nVeEQiS3XVx4KS/Xp1AQlbW4ZJOrPZUZYMZBFiTjvipeajBC8OOy7ww73kaNa2ju7Kt3/soPQopmjtSD8bOguVs06Uzj5gsv+jA3emE1ndGkDxiEd9uLPX9yVFPv1DWyS+TcPvvGgh928EL1/vvKLDc+wX8PztiiUguoW+zaZivT0Wkg1bHDCgQ==", Charset.forName("UTF-8")));

        HttpPost request = new HttpPost(url);
        request.setEntity(multipartEntity);
        request.addHeader("Content-Type", "Content-Disposition: form-data;");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        InputStream is = response.getEntity().getContent();
        response.getHeaders("");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null)
        {
            buffer.append(line);
        }

        System.out.println("发送消息收到的返回：" + buffer.toString());
    }

    public static void createUserCre1() {

        String url = "https://openapi.alipay.com/gateway.do?charset=UTF-8";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("method","alipay.trade.wap.com.ruoyi.xinsheng.pay");
        map.put("app_id","2021002190664476");
        map.put("timestamp","2022-12-10 13:01:59");
        map.put("format","json");
        map.put("version","1.0");
        map.put("alipay_sdk","alipay-easysdk-php-2.0.0");
        map.put("charset","UTF-8");
        map.put("sign_type","RSA2");
        map.put("biz_content","{\"subject\":\"CRMEB--米家 小米电动牙刷T302成人\\/学生 4种净齿....\",\"out_trade_no\":\"cp332868853002403840\",\"total_amount\":\"0.01\",\"quit_url\":\"\",\"product_code\":\"QUICK_WAP_WAY\",\"passback_params\":\"product\"}");
        map.put("notify_url","http://h5.mall2.yingliao.tv/api/pay/notify/alipay.html");
        map.put("sign","Bi4uTx924GpYa82Zh8CIbFJ3mCpoNcDC0zJnwdQAdpCRjty7yI2gxuylXBikUBhq9flCq3AWoSarcbLnE8bDzklf5bUUCuvTZb9Lr4eb3giYTrrVuqOyxeS9hYMmGleozL1TSaeBD/k6SiWI3FTaAH8FeSTshm5nVeEQiS3XVx4KS/Xp1AQlbW4ZJOrPZUZYMZBFiTjvipeajBC8OOy7ww73kaNa2ju7Kt3/soPQopmjtSD8bOguVs06Uzj5gsv+jA3emE1ndGkDxiEd9uLPX9yVFPv1DWyS+TcPvvGgh928EL1/vvKLDc+wX8PztiiUguoW+zaZivT0Wkg1bHDCgQ==");
       // String alipayUrlresultStr = HttpUtils.sendPost(url,JSONObject.toJSONString(map),null);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("json", JSONObject.toJSONString(map), ContentType.MULTIPART_FORM_DATA);

        HttpEntity multipart = builder.build();

        HttpResponse resp = null;
        try {
            httpPost.setEntity(multipart);
            resp = client.execute(httpPost);

            //注意，返回的结果的状态码是302，非200
            if (resp.getStatusLine().getStatusCode() == 302) {
                HttpEntity he = resp.getEntity();
                System.out.println("----------------123----------666666---");
                respContent = EntityUtils.toString(he, "UTF-8");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=========================:\t" + respContent);
        System.out.println("=========================:\t" + resp.getStatusLine().getStatusCode());

    }

    public static void createUserCre(String id) {
       // String url = "http://admin:11a8ff57440f35baead7a3cc8a21ec2c44@172.16.91.121:8888/jenkins/credentials/store/system/domain/_/createCredentials?";
        String url = "https://openapi.alipay.com/gateway.do?charset=UTF-8";
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        JSONObject jsonParam = new JSONObject();

        JSONObject credentialsJsonParam = new JSONObject();

        credentialsJsonParam.put("scope", "GLOBAL");
        //注意，如果ID一样的话，插入失败
        credentialsJsonParam.put("id", id);
        credentialsJsonParam.put("username", "abc520");
        credentialsJsonParam.put("password", "123456");
        credentialsJsonParam.put("description", "hello world jenkins hellow");

        jsonParam.put("credentials", credentialsJsonParam);
        jsonParam.put("", "0");

        System.out.println("=============:\t" + jsonParam.toString());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("json", jsonParam.toString(), ContentType.MULTIPART_FORM_DATA);

        HttpEntity multipart = builder.build();

        HttpResponse resp = null;
        try {
            httpPost.setEntity(multipart);
            resp = client.execute(httpPost);

            //注意，返回的结果的状态码是302，非200
            if (resp.getStatusLine().getStatusCode() == 302) {
                HttpEntity he = resp.getEntity();
                System.out.println("----------------123----------666666---");
                respContent = EntityUtils.toString(he, "UTF-8");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=========================:\t" + respContent);
        System.out.println("=========================:\t" + resp.getStatusLine().getStatusCode());

    }


    public static String sendRequest() throws Exception{
        HttpResponse response = null;
        try {
            // 创建HttpClient实例及Post方法
            CloseableHttpClient httpclient = new DefaultHttpClient();
            String url = "https://openapi.alipay.com/gateway.do?charset=UTF-8";
            String respContent = "";
            HttpPost httppost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //因为传入的值为汉字，所以使用ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8)进行一个字符的转换，否则将会出现乱码。字母和数字不需要使用。
            builder.addTextBody("method", "alipay.trade.wap.com.ruoyi.xinsheng.pay", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("app_id", "2021002190664476", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("timestamp", "2022-12-10 13:01:59", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("format", "json", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("version", "1.0", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("alipay_sdk", "alipay-easysdk-php-2.0.0", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("charset", "UTF-8", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("sign_type", "RSA2", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("biz_content", "{\"subject\":\"CRMEB--米家 小米电动牙刷T302成人\\/学生 4种净齿....\",\"out_trade_no\":\"cp332868853002403840\",\"total_amount\":\"0.01\",\"quit_url\":\"\",\"product_code\":\"QUICK_WAP_WAY\",\"passback_params\":\"product\"}", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("notify_url", "http://h5.mall2.yingliao.tv/api/pay/notify/alipay.html", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));
            builder.addTextBody("sign", "Bi4uTx924GpYa82Zh8CIbFJ3mCpoNcDC0zJnwdQAdpCRjty7yI2gxuylXBikUBhq9flCq3AWoSarcbLnE8bDzklf5bUUCuvTZb9Lr4eb3giYTrrVuqOyxeS9hYMmGleozL1TSaeBD/k6SiWI3FTaAH8FeSTshm5nVeEQiS3XVx4KS/Xp1AQlbW4ZJOrPZUZYMZBFiTjvipeajBC8OOy7ww73kaNa2ju7Kt3/soPQopmjtSD8bOguVs06Uzj5gsv+jA3emE1ndGkDxiEd9uLPX9yVFPv1DWyS+TcPvvGgh928EL1/vvKLDc+wX8PztiiUguoW+zaZivT0Wkg1bHDCgQ==", ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8));

            HttpEntity multipart = builder.build();
            httppost.setEntity(multipart);
            response =httpclient.execute(httppost);// 发送请求
            System.out.println(response.getStatusLine().getStatusCode());

            //注意，返回的结果的状态码是302，非200
            if (response.getStatusLine().getStatusCode() == 302) {
                HttpEntity he = response.getEntity();
                System.out.println(response.getHeaders("Location"));
                respContent = EntityUtils.toString(he, "UTF-8");
                String line="";
                StringBuilder result = new StringBuilder();
                int count = response.getAllHeaders().length;
                for (int i=0;i<count;i++) {
                    if("Location".equals(response.getAllHeaders()[i].getName()) ){
                        String str =response.getAllHeaders()[i].getValue();
                        return str;
                    }
                }
            }
            httppost.releaseConnection();
            httpclient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}










