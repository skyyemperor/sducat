package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-01-30 21:45
 * Description : 疫苗接种状态枚举类
 */
@Getter
public enum VaccinationEnum {

    NOT_VACCINATED(0, "未接种"),
    HAVE_VACCINATED(1, "已接种"),
    ING(2, "接种进行中"),
    ;


    private final Integer key;
    private final String remark;

    VaccinationEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (VaccinationEnum enums : VaccinationEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }
}
