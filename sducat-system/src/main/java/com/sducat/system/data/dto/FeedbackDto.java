package com.sducat.system.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sducat.common.core.data.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by skyyemperor on 2021-02-15 19:59
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    /**
     * 反馈ID
     */
    private Long feedbackId;

    private String submitUserName;

    /**
     * 猫咪
     */
    private CatLessDto cat;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 反馈时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 是否已审核过
     */
    private Integer checked;

}
