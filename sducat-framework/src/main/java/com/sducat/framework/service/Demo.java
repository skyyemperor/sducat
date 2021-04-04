package com.sducat.framework.service;

import com.sducat.common.util.AESUtil;
import com.sducat.common.util.JSONUtil;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.http.OkHttpUtil;
import com.sducat.framework.filter.SignFilter;

import java.util.*;

/**
 * Created by skyyemperor on 2021-01-30 15:19
 * Description :
 */
public class Demo {

    private static String encrypt(Map<String, String> params) {
        long date = System.currentTimeMillis();
        System.out.println(date); //请求的时间
        params.put("date", String.valueOf(date));

        List<String> box = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            box.add(entry.getKey());
            box.add(entry.getValue());
        }

        //把参数和值都放入box，然后排序
        Collections.sort(box);

        //明文就是box的值用‘#’分割
        StringBuilder plainText = new StringBuilder();
        for (String str : box) {
            plainText.append(str).append('#');
        }
        plainText.deleteCharAt(plainText.length() - 1);

        String key = AESUtil.generateKey(); //前端生成key密钥
        String cipherText = AESUtil.encryptData(key, plainText.toString()); //加密

        //截取密文的前16位
        if (cipherText != null && cipherText.length() > 16) {
            cipherText = cipherText.substring(0, 16);
        }

        //将加密后的密文和密钥放在一起
        return cipherText + key;
    }


    public static void main(String[] args) throws Exception {
        Demo demo = new Demo();

    }
}
