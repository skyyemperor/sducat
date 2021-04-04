package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sducat.common.annotation.validation.EnumValidation;
import com.sducat.common.enums.*;
import com.sducat.system.data.dto.CatLessDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * Created by skyyemperor on 2021-01-30 17:00
 * Description :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "cat")
public class Cat implements Serializable {
    /**
     * 猫咪ID
     */
    @NotNull(groups = Update.class, message = "参数catId不能为null")
    @Null(groups = Insert.class, message = "参数catId必须为null")
    @TableId(value = "cat_id", type = IdType.AUTO)
    private Long catId;

    /**
     * 猫咪名称
     */
    @Size(max = 50, message = "参数catName不能超过50个字符")
    @NotNull(groups = Insert.class, message = "参数catName不能为空")
    @TableField(value = "cat_name")
    private String catName;

    /**
     * 在校状态。0在校，1待领养，2被领养，3外出流浪，4喵星
     */
    @EnumValidation(clazz = CatStatusEnum.class, message = "参数status不合要求")
    @NotNull(groups = Insert.class, message = "参数status不能为空")
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 猫咪性别(公、母、不明)
     */
    @EnumValidation(clazz = SexEnum.class, message = "参数sex不合要求")
    @NotNull(groups = Insert.class, message = "参数sex不能为空")
    @TableField(value = "sex")
    private String sex;

    /**
     * 生日
     */
    @TableField(value = "birthday")
    private LocalDate birthday;

    /**
     * 外形
     */
    @Size(max = 500, message = "参数shape不能超过500个字符")
    @TableField(value = "shape")
    private String shape;

    /**
     * 花色
     */
    @Size(max = 20, message = "参数color不能超过500个字符")
    @NotNull(groups = Insert.class, message = "参数花色不能为空")
    @EnumValidation(clazz = CatColorEnum.class, message = "没有这个花色哦")
    @TableField(value = "color")
    private String color;

    /**
     * 性格
     */
    @Size(max = 500, message = "参数character不能超过500个字符")
    @TableField(value = "`character`")
    private String character;

    /**
     * 校区
     */
    @EnumValidation(clazz = CampusEnum.class, message = "参数campus不合要求")
    @NotNull(groups = Insert.class, message = "参数campus不能为空")
    @TableField(value = "campus")
    private String campus;

    /**
     * 常驻地
     */
    @Size(max = 100, message = "参数usualPlace不能超过100个字符")
    @TableField(value = "usual_place")
    private String usualPlace;

    /**
     * 疫苗接种状态(0未接种，1已接种，2接种进行中)
     */
    @EnumValidation(clazz = VaccinationEnum.class, message = "参数vaccination不合要求")
    @TableField(value = "vaccination")
    private Integer vaccination;

    /**
     * 绝育状态(0未绝育，1已绝育)
     */
    @EnumValidation(clazz = SterilizationEnum.class, message = "参数sterilization不合要求")
    @TableField(value = "sterilization")
    private Integer sterilization;

    /**
     * 健康状况
     */
    @Size(max = 500, message = "参数health不能超过500个字符")
    @TableField(value = "health")
    private String health;

    /**
     * 注意事项
     */
    @Size(max = 500, message = "参数note不能超过500个字符")
    @TableField(value = "note")
    private String note;

    /**
     * 领养说明
     */
    @Size(max = 500, message = "参数adoptNote不能超过500个字符")
    @TableField(value = "adopt_note")
    private String adoptNote;

    /**
     * 社会关系
     */
    @Size(max = 500, message = "参数relation不能超过500个字符")
    @TableField(value = "relation")
    private String relation;

    /**
     * 猫咪圆形图片
     */
    @NotNull(groups = Insert.class, message = "参数roundPic不能为空")
    @TableField(value = "round_pic")
    private String roundPic;

    /**
     * 猫咪主图（外部卡片以及领养页面卡片）
     */
    @NotNull(groups = Insert.class, message = "参数mainPic不能为空")
    @TableField(value = "main_pic")
    private String mainPic;

    /**
     * 滑动图片数组
     */
    @TableField(value = "slide_pic")
    private String slidePic;

    /**
     * 与该猫咪有关系的猫咪ID
     */
    @Null
    @TableField(exist = false)
    private List<CatLessDto> relatives;

    public Cat(Long catId) {
        this.catId = catId;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_CAT_ID = "cat_id";

    public static final String COL_CAT_NAME = "cat_name";

    public static final String COL_STATUS = "`status`";

    public static final String COL_SEX = "sex";

    public static final String COL_AGE = "age";

    public static final String COL_SHAPE = "shape";

    public static final String COL_COLOR = "color";

    public static final String COL_CHARACTER = "`character`";

    public static final String COL_CAMPUS = "campus";

    public static final String COL_USUAL_PLACE = "usual_place";

    public static final String COL_VACCINATION = "vaccination";

    public static final String COL_STERILIZATION = "sterilization";

    public static final String COL_ADOPTION = "adoption";

    public static final String COL_HEALTH = "health";

    public static final String COL_NOTE = "note";

    public static final String COL_ADOPT_NOTE = "adopt_note";

    public static final String COL_RELATION = "relation";

    public static final String COL_ROUND_PIC = "round_pic";

    public static final String COL_MAIN_PIC = "main_pic";

    public static final String COL_SLIDE_PIC = "slide_pic";
}