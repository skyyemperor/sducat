package com.sducat.common.util.wx;

import com.sducat.common.util.JSONUtil;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by skyyemperor on 2021-02-11 18:37
 * Description : 获取微信小程序accessToken的工具类
 */
@Component
public class AccessTokenUtil {

    @Value("${weixin_app.app_id}")
    private String appId;

    @Value("${weixin_app.app_secret}")
    private String appSecret;

    private String accessToken;

    /**
     * 请求获得accessToken时的时间戳
     */
    private Long requestTime = 0L;

    /**
     * == 1.9 * 3600 * 1000 过期时间2小时,这里设为1.9小时
     */
    private static final Long EXPIRE_TIME = 19 * 3600 * 100L;

    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";


    public synchronized String getAccessToken() {
        if (System.currentTimeMillis() - requestTime > EXPIRE_TIME) {
            String res = OkHttpUtil.getInstance().get(
                    GET_ACCESS_TOKEN_URL,
                    MapBuildUtil.builder()
                            .data("grant_type", "client_credential")
                            .data("appid", appId)
                            .data("secret", appSecret)
                            .get());
            accessToken = JSONUtil.getFieldFromJson(res, "access_token");
            requestTime = System.currentTimeMillis();
        }
        return accessToken;
    }


}
