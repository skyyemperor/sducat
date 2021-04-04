package com.sducat.framework.filter;

import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.AESUtil;
import com.sducat.common.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by skyyemperor on 2021-01-30 14:00
 * Description : 参数校验Filter
 */
public class SignFilter implements Filter {

    private static final String SIGN_HEADER = "SIGN";

    private static final String TIMESTAMP_HEADER = "STAMP";

    private static final HashSet<String> URLS = new HashSet<>(
            Arrays.asList("/sducat/auth/login", "/sducat/auth/admin/login")
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!URLS.contains(((HttpServletRequest) servletRequest).getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse); //跳过这些请求的校验
            return;
        }

        if(!verifyTime()){
            ServletUtil.responseResult((HttpServletResponse) servletResponse,
                    Result.getResult(CommonError.REQUEST_TIMEOUT));
            return;
        }

        if (verifyParam()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ServletUtil.responseResult((HttpServletResponse) servletResponse,
                    Result.getResult(CommonError.VERIFY_WRONG));
        }
    }

    @Override
    public void destroy() {

    }

    private boolean verifyParam() {
        String sign = ServletUtil.getRequest().getHeader(SIGN_HEADER);
        if (sign == null || sign.length() != 32) return false; //检查SIGN请求头是否存在
        String cipherText = sign.substring(0, 16);
        String key = sign.substring(sign.length() - 16);

        //请求的时间戳
        String timestamp = ServletUtil.getRequest().getHeader(TIMESTAMP_HEADER);

        Map<String, String> params = ServletUtil.getAllParameters();

        List<String> box = new ArrayList<>();
        box.add(timestamp);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            box.add(entry.getKey());
            box.add(entry.getValue());
        }
        Collections.sort(box);

        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < box.size(); i++) {
            if (i > 0) plainText.append('#');
            plainText.append(box.get(i));
        }

        //AES加密校验
        String desCipherText = AESUtil.encryptData(key, plainText.toString());
        if (desCipherText != null && desCipherText.length() > 16) {
            desCipherText = desCipherText.substring(0, 16);
        }
        return cipherText.equals(desCipherText);
    }

    private boolean verifyTime(){
        //限制请求时间为10分钟内
        String timestamp = ServletUtil.getRequest().getHeader(TIMESTAMP_HEADER);
        try {
            long timeSub = System.currentTimeMillis() - Long.parseLong(timestamp);
            if (Math.abs(timeSub) > 300 * 1000L) {
                System.out.println("时间差：" + timeSub);
                System.out.println(System.currentTimeMillis() + "---" + timestamp);
                throw new RuntimeException(); //请求时间与当前时间差大于60秒
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        //假设使用 /sducat/cat/search?status=4 这个接口
        //那么用一个list ，里面存入所有的key，value和时间戳，此接口即 status 4 1615443968468(当前时间戳)
        //然后把list排序，之后用'#'分隔生成一段明文 1615443968468#4#status
        //之后就是加密，随机生成一段长为16的key，使用偏移量 "pian-yi-lianghhh"(固定的) 加密得到密文
        //截取密文前16位，之后在其后拼接上那个随机生成的key，共32位字串，放于 SIGN 请求头
        //同时 上面用到的时间戳也需要放于请求头 _t
        Map<String, String> map = new HashMap<>();
        map.put("userName", "sducat_admin");
        map.put("password", "lovely_sducat");
        System.out.println(new SignFilter().encrypt(map));
    }

    /**
     * 加密
     */
    private String encrypt(Map<String, String> params) {
//        Long timestamp = System.currentTimeMillis();
        Long timestamp = 1615463198535L;
        System.out.println(timestamp);

        List<String> box = new ArrayList<>();
        box.add(timestamp.toString());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            box.add(entry.getKey());
            box.add(entry.getValue());
        }
        Collections.sort(box);

        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < box.size(); i++) {
            if (i > 0) plainText.append('#');
            plainText.append(box.get(i));
        }

        String key = "456e109902d81e3e"; //随机生成的key(用来加密)
//        String key = AESUtil.generateKey(); //随机生成的key(用来加密)
        String cipherText = AESUtil.encryptData(key, plainText.toString()).substring(0, 16); //加密后的密文

        return cipherText + key; //合并在一起
    }
}
