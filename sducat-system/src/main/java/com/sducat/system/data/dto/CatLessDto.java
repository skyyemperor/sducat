package com.sducat.system.data.dto;

import com.alibaba.fastjson.JSON;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.common.enums.SterilizationEnum;
import com.sducat.common.enums.VaccinationEnum;
import com.sducat.system.data.po.Cat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by skyyemperor on 2021-01-30 22:14
 * Description : cat卡片页dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatLessDto implements Serializable {

    /**
     * 猫咪ID
     */
    private Long catId;

    /**
     * 猫咪名称
     */
    private String catName;

    /**
     * 在校状态()
     */
    private String status;

    /**
     * 猫咪性别(公、母)
     */
    private String sex;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 校区
     */
    private String campus;

    /**
     * 性格
     */
    private String character;

    /**
     * 猫咪圆形图片
     */
    private String roundPic;

    /**
     * 猫咪主图（外部卡片以及领养页面卡片）
     */
    private String mainPic;


    public static CatLessDto getDto(Cat cat) {
        return new CatLessDto(
                cat.getCatId(),
                cat.getCatName(),
                CatStatusEnum.getRemark(cat.getStatus()),
                cat.getSex(),
                cat.getBirthday(),
                cat.getCampus(),
                cat.getCharacter(),
                cat.getRoundPic(),
                cat.getMainPic()
        );
    }

    private static final long serialVersionUID = 1L;
}
