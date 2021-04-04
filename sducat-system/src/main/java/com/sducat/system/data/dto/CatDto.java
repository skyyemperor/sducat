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
import java.util.List;

/**
 * Created by skyyemperor on 2021-01-30 22:14
 * Description : cat详情页的dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatDto implements Serializable {

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
     * 外形
     */
    private String shape;

    /**
     * 花色
     */
    private String color;

    /**
     * 性格
     */
    private String character;

    /**
     * 校区
     */
    private String campus;

    /**
     * 常驻地
     */
    private String usualPlace;

    /**
     * 疫苗接种状态(0未接种，1已接种，2接种进行中)
     */
    private String vaccination;

    /**
     * 绝育状态(0未绝育，1已绝育)
     */
    private String sterilization;

    /**
     * 健康状况
     */
    private String health;

    /**
     * 注意事项
     */
    private String note;

    /**
     * 领养说明
     */
    private String adoptNote;

    /**
     * 社会关系
     */
    private String relation;

    /**
     * 猫咪圆形图片
     */
    private String roundPic;

    /**
     * 猫咪主图（外部卡片以及领养页面卡片）
     */
    private String mainPic;

    /**
     * 滑动图片数组
     */
    private List<String> slidePic;

    /**
     * 与该猫咪有关系的猫咪ID
     */
    private List<CatLessDto> relatives;


    public static CatDto getDto(Cat cat) {
        return new CatDto(
                cat.getCatId(),
                cat.getCatName(),
                CatStatusEnum.getRemark(cat.getStatus()),
                cat.getSex(),
                cat.getBirthday(),
                cat.getShape(),
                cat.getColor(),
                cat.getCharacter(),
                cat.getCampus(),
                cat.getUsualPlace(),
                VaccinationEnum.getRemark(cat.getVaccination()),
                SterilizationEnum.getRemark(cat.getSterilization()),
                cat.getHealth(),
                cat.getNote(),
                cat.getAdoptNote(),
                cat.getRelation(),
                cat.getRoundPic(),
                cat.getMainPic(),
                JSON.parseArray(cat.getSlidePic(), String.class),
                cat.getRelatives()
        );
    }

    private static final long serialVersionUID = 1L;
}
