package com.sducat.system.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sducat.common.util.JSONUtil;
import com.sducat.system.data.po.NewCat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by skyyemperor on 2021-02-18 17:35
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCatDto {

    private Long newCatId;

    /**
     * 提交的用户的ID
     */
    private String submitUserName;

    /**
     * 校区
     */
    private String campus;

    /**
     * 目击地点
     */
    private String witnessLocation;

    /**
     * 目击时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String witnessTime;

    /**
     * 猫咪详情
     */
    private String description;

    /**
     * 图片链接数组
     */
    private List<String> pic;

    /**
     * 提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    private Integer checked;

    public static NewCatDto getDto(NewCat cat) {
        return new NewCatDto(
                cat.getNewCatId(),
                cat.getSubmitUserId().toString(),
                cat.getCampus(),
                cat.getWitnessLocation(),
                cat.getWitnessTime(),
                cat.getDescription(),
                JSONUtil.getStringArray(cat.getPic()),
                cat.getSubmitTime(),
                cat.getChecked()
        );
    }
}
