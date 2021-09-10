package com.sducat.common.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36 Edg/91.0.864.71";

    private static OkHttpUtil instance;

    private OkHttpClient client;

    private OkHttpUtil() {
        client = new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtil.class) {
                if (instance == null) instance = new OkHttpUtil();
            }
        }
        return instance;
    }

    //允许的后缀
    private static final HashMap<String, String> ALLOW_SUFFIX = new HashMap<String, String>() {
        {
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
            put("gif", "image/gif");
            put("bmp", "image/bmp");
            put("webp", "image/webp");
        }
    };

    //------------------------------------------------------------------//
    //------------------------------get请求------------------------------//
    //------------------------------------------------------------------//
    public String get(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        Request.Builder builder = new Request.Builder().get().url(buildHttpGet(url, bodyMap));
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    //------请求头只包含cookie------//
    public String get(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return get(url, bodyMap, headerMap);
    }

    //------请求头为空------//
    public String get(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return get(url, bodyMap, headerMap);
    }

    //------请求头和请求题均为空------//
    public String get(String url) {
        Map<String, Object> nullMap = new HashMap<>();
        return get(url, nullMap, nullMap);
    }

    //------返回Response------//
    public Response get0(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        Request.Builder builder = new Request.Builder().get().url(buildHttpGet(url, bodyMap));
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return execute(request);
    }

    public Response get0(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return get0(url, bodyMap, headerMap);
    }

    public Response get0(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return get0(url, bodyMap, headerMap);
    }

    public Response get0(String url) {
        Map<String, Object> nullMap = new HashMap<>();
        return get0(url, nullMap, nullMap);
    }

    //------------------------------------------------------------------//
    //------------------------post方式(Json请求)------------------------//
    //------------------------------------------------------------------//
    public String postJson(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByJson(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    public String postJson(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postJson(url, bodyMap, headerMap);
    }

    public String postJson(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postJson(url, bodyMap, headerMap);
    }

    public Response postJson0(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByJson(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return execute(request);
    }

    public Response postJson0(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postJson0(url, bodyMap, headerMap);
    }

    public Response postJson0(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postJson0(url, bodyMap, headerMap);
    }

    //------------------------------------------------------------------//
    //------------------------post方式(表单请求)------------------------//
    //------------------------------------------------------------------//
    public String postForm(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByMap(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    public String postForm(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postForm(url, bodyMap, headerMap);
    }

    public String postForm(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postForm(url, bodyMap, headerMap);
    }

    public Response postForm0(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByMap(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return execute(request);
    }

    public Response postForm0(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postForm0(url, bodyMap, headerMap);
    }

    public Response postForm0(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postForm0(url, bodyMap, headerMap);
    }

    //------------------------------------------------------------------//
    //--------------------post方式(Multipart表单请求)--------------------//
    //------------------------------------------------------------------//
    public String postMultiFile(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByMultipartFile(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return executeString(request);
    }

    public String postMultiFile(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postMultiFile(url, bodyMap, headerMap);
    }

    public String postMultiFile(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postMultiFile(url, bodyMap, headerMap);
    }

    public Response postMultiFile0(String url, Map<String, Object> bodyMap, Map<String, Object> headerMap) {
        RequestBody body = buildRequestBodyByMultipartFile(bodyMap);
        Request.Builder builder = new Request.Builder().post(body).url(url);
        addHeaders(builder, headerMap);
        Request request = builder.build();

        return execute(request);
    }

    public Response postMultiFile0(String url, Map<String, Object> bodyMap, String cookie) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", cookie);
        return postMultiFile0(url, bodyMap, headerMap);
    }

    public Response postMultiFile0(String url, Map<String, Object> bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        return postMultiFile0(url, bodyMap, headerMap);
    }


    public String executeString(Request request) {
        try {
            return client.newCall(request).execute().body().string();
        } catch (Exception e) {
            log.error("【" + request.url() + "】" + "请求失败");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 同步执行
     */
    public Response execute(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            log.error("【" + request.url() + "】" + "请求失败");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步执行
     */
    public void enqueue(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 异步执行, 且不在意返回结果
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

    /**
     * 判断后缀是否合法
     *
     * @return 合法返回 res[0]:后缀名;  res[1]:Content-Type
     * 不合法返回 空数组
     */
    public String[] dealSuffix(String fileName) {
        if (fileName == null || !fileName.contains(".")) return new String[]{};

        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (ALLOW_SUFFIX.containsKey(suffix)) {
            return new String[]{suffix, ALLOW_SUFFIX.get(suffix)};
        }

        return new String[]{};
    }

    /**
     * 添加header信息
     */
    private static void addHeaders(Request.Builder builder, Map<String, Object> headerMap) {
        builder.addHeader("User-Agent", USER_AGENT);
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
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
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
     * multipart_file方式提交构建
     */
    private RequestBody buildRequestBodyByMultipartFile(Map<String, Object> bodyParams) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (bodyParams != null) {
            for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                Object v = entry.getValue();
                if (v instanceof File) {
                    File file = (File) v;
                    builder.addFormDataPart(entry.getKey(), file.getName(),
                            RequestBody.create(file, MediaType.parse(dealSuffix(file.getName())[1])));
                } else if (v instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) v;
                    try {
                        builder.addFormDataPart(entry.getKey(), file.getOriginalFilename(),
                                RequestBody.create(file.getBytes(), MediaType.parse(dealSuffix(file.getOriginalFilename())[1])));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return builder.build();
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
}