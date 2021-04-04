package com.sducat.common.util.http;


import okhttp3.*;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpUtil instance;

    private OkHttpClient client;

    private OkHttpUtil() {
        client = new OkHttpClient.Builder()
                .callTimeout(8, TimeUnit.SECONDS)
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtil.class) {
                if (instance == null) {
                    instance = new OkHttpUtil();
                }
            }
        }
        return instance;
    }

    /**
     * get请求，同步方式
     */
    public String getData(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        //1 构造Request
        Request.Builder builder = new Request.Builder().get().url(buildHttpGet(url, bodyMap));
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }


    /**
     * post方式(Json请求)
     */
    public String postDataByJson(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByJson(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    /**
     * post方式(表单请求)
     */
    public String postDataByMap(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByMap(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    /**
     * 添加header信息
     */
    private static void addHeaders(Request.Builder builder, Map<String, Object> headerMap) {
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    /**
     * 表单方式提交构建
     */
    private RequestBody buildRequestBodyByMap(Map<String, Object> bodyParams) {
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (bodyParams != null) {
            for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                formEncodingBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return formEncodingBuilder.build();
    }


    /**
     * json方式提交构建
     */
    private RequestBody buildRequestBodyByJson(Map<String, Object> bodyParams) {
        String jsonStr = "{}";
        if (bodyParams != null && !bodyParams.isEmpty()) {
            jsonStr = com.alibaba.fastjson.JSON.toJSONString(bodyParams);
        }
        return RequestBody.create(jsonStr, JSON);
    }


    /**
     * 构建http get请求，将参数拼接到url后面
     */
    private static String buildHttpGet(String url, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            builder.append('?');
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.append(entry.getKey())
                        .append('=')
                        .append(entry.getValue())
                        .append('&');
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    public String executeString(Request request) {
        try {
            return client.newCall(request).execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public void enqueue(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public void enqueue(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

}