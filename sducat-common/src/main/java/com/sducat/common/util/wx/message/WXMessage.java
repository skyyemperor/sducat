package com.sducat.common.util.wx.message;

import com.alibaba.fastjson.JSONObject;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.TaskExecutorUtil;
import com.sducat.common.util.OkHttpUtil;
import com.sducat.common.util.wx.AccessTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skyyemperor on 2021-02-11 19:06
 * Description :
 */
@Component
public abstract class WXMessage {

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    private static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    protected abstract String getTemplateId();

    protected abstract ArrayList<String> getKeys();

    protected String getPage() {
        return null;
    }

    public void send(String openId, String... data) {
        taskExecutorUtil.run(() -> {
            String res = OkHttpUtil.getInstance().postJson(
                    SEND_MESSAGE_URL + "?access_token=" + accessTokenUtil.getAccessToken(),
                    MapBuildUtil.builder().data("touser", openId)
                            .data("template_id", getTemplateId())
                            .data("data", buildData(data))
                            .get());
            System.out.println(res);
        });
    }

    private Map<String, Object> buildData(String... data) {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> keys = getKeys();
        for (int i = 0; i < keys.size(); i++) {
            JSONObject value = new JSONObject();
            value.put("value", data[i]);
            map.put(keys.get(i), value);
        }
        return map;
    }
}
