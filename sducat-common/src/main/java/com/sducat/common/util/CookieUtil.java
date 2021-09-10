package com.sducat.common.util;

import okhttp3.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by skyyemperor on 2020-11-27 11:42
 * Description : 存放cookie的工具类
 */
public class CookieUtil {
    public static String getCookie(Response response) {
        StringBuilder res = new StringBuilder();
        for (String cookie : response.headers("Set-Cookie")) {
            Matcher matcher = Pattern.compile("^((.*?)=.*?;).+((?i)Path)=(.*?);").matcher(cookie + ";");
            if (matcher.find()) {
                //匹配的样例：JSESSIONID=C030F6B4FF2E077E371; Path=/cas; HttpOnly
                //group(1):JSESSIONID=C030F6B4FF2E077E371;
                //group(2):JSESSIONID
                //group(4):/cas
                res.append(matcher.group(1));
            }
        }
        return res.toString();
    }
}
