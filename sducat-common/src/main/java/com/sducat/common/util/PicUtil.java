package com.sducat.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created by skyyemperor on 2021-07-22
 * Description :
 */
@Component
public class PicUtil {

    @Value("${pic.host}")
    private String host;

    public boolean checkPicHost(String... picUrls) {
        if(picUrls == null) return true;
        for (String url : picUrls) {
            if (!(url.startsWith("https://" + host) || url.startsWith("http://" + host)))
                return false;
        }
        return true;
    }

    public boolean checkPicHost(List<String> picUrls) {
        if(picUrls == null) return true;
        for (String url : picUrls) {
            if (!(url.startsWith("https://" + host) || url.startsWith("http://" + host)))
                return false;
        }
        return true;
    }

}
