package com.sducat.system.data.dto;

import com.sducat.common.enums.SterilizationEnum;
import com.sducat.common.enums.VaccinationEnum;
import com.sducat.system.data.po.Cat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by skyyemperor on 2021-02-28
 * Description : 领养界面的猫咪DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatAdoptDto implements Serializable {
    /**
     * 猫咪ID
     */
    private Long catId;

    /**
     * 猫咪名称
     */
    private String catName;

    /**
     * 猫咪性别(公、母)
     */
    private String sex;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 疫苗接种状态(0未接种，1已接种，2接种进行中)
     */
    private String vaccination;

    /**
     * 绝育状态(0未绝育，1已绝育)
     */
    private String sterilization;

    /**
     * 领养说明
     */
    private String adoptNote;

    /**
     * 猫咪主图（外部卡片以及领养页面卡片）
     */
    private String mainPic;


    public static CatAdoptDto getDto(Cat cat) {
        return new CatAdoptDto(
                cat.getCatId(),
                cat.getCatName(),
                cat.getSex(),
                cat.getBirthday(),
                VaccinationEnum.getRemark(cat.getVaccination()),
                SterilizationEnum.getRemark(cat.getSterilization()),
                cat.getAdoptNote(),
                cat.getMainPic()
        );
    }

    private static final long serialVersionUID = 1L;
}
