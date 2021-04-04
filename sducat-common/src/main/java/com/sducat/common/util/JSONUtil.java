package com.sducat.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JSONUtil {

    //将对象转化为json
    public static String toJson(Object object) {
        return toJson(object, 1);
    }

    public static String toJson(Object object, int flag) {
        final String flag1 = "yyyy-MM-dd";
        final String flag2 = "yyyy-MM-dd HH:mm";
        String endFlag = null;

        switch (flag) {
            case 0:
                break;
            case 1:
                endFlag = flag1;
                break;
            case 2:
                endFlag = flag2;
                break;
        }
        return JSON.toJSONStringWithDateFormat(object, endFlag);
    }

    //将json转化为对象
    public static <T> T fromJson(Class<T> clazz, String json, String... fields) {
        T tb = null;
        try {
            tb = JSON.parseObject(getFieldFromJson(json, fields), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tb;
    }

    //获取json的某个字段
    public static String getFieldFromJson(String json, String... fields) {
        JSONObject jsonObject;
        for (String field : fields) {
            if (json != null && !"".equals(json)) {
                jsonObject = JSON.parseObject(json);
                json = jsonObject.getString(field);
            }
        }
        return json;
    }

    //将json中data对应的dataJson转化为对象
    public static <T> T getDataFromJson(String json, Class<T> clazz) {
        String dataJson = JSON.parseObject(json).getString("data");
        return fromJson(clazz, dataJson);
    }

    //将json转化为Integer数组，例如"[5,7,8]"
    public static List<Integer> getIntArray(String arrayJson) {
        return JSON.parseArray(arrayJson, Integer.class);
    }

    public static List<Integer> addIntArray(String arrayJson, Integer num) {
        List<Integer> array = getIntArray(arrayJson);
        boolean flag = false;
        for (Integer n : array) {
            if (n.equals(num)) {
                flag = true;
            }
        }
        if (!flag) {
            array.add(num);
        }
        return array;
    }

    public static List<Integer> removeIntArray(String arrayJson, Integer num) {
        List<Integer> array = getIntArray(arrayJson);
        array.remove(num);
        return array;
    }

    //将json转化为String数组，例如"["hebf","egh2u"]"
    public static List<String> getStringArray(String arrayJson) {
        return JSON.parseArray(arrayJson, String.class);
    }

}
