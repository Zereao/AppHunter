package com.zereao.apphunter.common.tools;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Zereao
 * @version 2019/05/09  15:32
 */
@Slf4j
public class OkHttp3Utils {
    /**
     * ContentType 内部枚举
     */
    public enum ContentType {
        /**
         * JSON数据 - UTF-8
         */
        JSON_UTF8("application/json; charset=utf-8"),
        /**
         * 表单类型数据 - UTF-8
         */
        FORM_UTF8("application/x-www-form-urlencoded; charset=utf-8");

        private MediaType type;

        ContentType(String type) {
            this.type = MediaType.parse(type);
        }

        public MediaType getType() {
            return type;
        }
    }

    private enum Instance {
        /**
         * 单元素枚举实体
         */
        INSTANCE;

        private OkHttpClient client;

        Instance() {
            client = new OkHttpClient.Builder()
                    // 设置连接超时
                    .callTimeout(10, TimeUnit.SECONDS)
                    // 设置读超时
                    .readTimeout(10, TimeUnit.SECONDS)
                    // 设置写超时
                    .writeTimeout(10, TimeUnit.SECONDS)
                    // 是否自动重连
                    .retryOnConnectionFailure(true)
                    // 设置连接池
                    .connectionPool(new ConnectionPool(100, 30, TimeUnit.MINUTES))
                    .build();
        }
    }

    /**
     * 获取单例的OkHttpClient实例，方便自定义方法
     *
     * @return 单例的OkHttpClient实例
     */
    public static OkHttpClient getClient() {
        return Instance.INSTANCE.client;
    }

    /**
     * 发送GET请求，并返回InputStream
     *
     * @param url 请求Url链接
     * @return 返回的输入流
     */
    public static InputStream doGetStream(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            ResponseBody body = sendRequest(request);
            return Optional.ofNullable(body).map(ResponseBody::byteStream).orElse(null);
        } catch (IOException e) {
            log.error("请求失败！", e);
        }
        return null;
    }

    /**
     * 发送Get请求，并返回UTF-8编码的结果
     *
     * @param url 请求Url链接
     * @return 请求结果
     */
    public static String doGet(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            ResponseBody body = sendRequest(request);
            return body == null ? null : body.string();
        } catch (IOException e) {
            log.error("请求失败！", e);
        }
        return null;
    }

    /**
     * 发送Get请求，并返回GBK编码的就过
     *
     * @param url 请求Url链接
     * @return 请求结果
     * @throws IOException IOException
     */
    public static String doGetWithGBK(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try {
            ResponseBody body = sendRequest(request);
            return body == null ? null : new String(body.bytes(), "GBK");
        } catch (IOException e) {
            log.error("请求失败！", e);
        }
        return null;
    }

    /**
     * 发送同步的Post请求，以表单方式传递参数
     *
     * @param url    请求Url链接
     * @param params 请求参数
     * @return 请求结果
     */
    public static String doFormPost(String url, Map<String, Object> params) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        params.forEach((key, value) -> formBodyBuilder.add(key, String.valueOf(value)));
        Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        try {

            ResponseBody body = sendRequest(request);
            return body == null ? null : body.string();
        } catch (IOException e) {
            log.error("请求失败！", e);
        }
        return null;
    }


    /**
     * 抽离的私有方法，发送请求，并对处理Response
     *
     * @param request Request
     * @return 解析出的结果字符串
     * @throws IOException IOException
     */
    private static ResponseBody sendRequest(Request request) throws IOException {
        Response response = Instance.INSTANCE.client.newCall(request).execute();
        return response.body();
    }
}
