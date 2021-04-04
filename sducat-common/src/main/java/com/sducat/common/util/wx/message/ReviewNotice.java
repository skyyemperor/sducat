package com.sducat.common.util.wx.message;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by skyyemperor on 2021-02-11 19:27
 * Description : 审核结果通知
 */
@Component
public class ReviewNotice extends WXMessage {

    @Override
    protected String getTemplateId() {
        return "fhc20a4A52BuEtC_1Ou6UREjEWVBr1KslRthZqN-4B4";
    }

    @Override
    protected ArrayList<String> getKeys() {
        return new ArrayList<>(Arrays.asList(
                "date4", //提交时间
                "date3", //审核时间
                "thing2", //审核内容
                "phrase1", //审核结果
                "thing7" //备注
        ));
    }


}
