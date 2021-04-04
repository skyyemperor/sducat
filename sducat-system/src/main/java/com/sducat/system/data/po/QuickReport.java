package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sducat.common.annotation.validation.EnumValidation;
import com.sducat.common.enums.SkipLinkTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * Created by skyyemperor on 2021-02-23
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "quick_report")
public class QuickReport implements Serializable {
    /**
     * 速报ID
     */
    @Null(message = "不能存在reportId参数")
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    /**
     * 速报标题
     */
    @NotNull(message = "速报标题不能为空")
    @Size(min = 1, max = 20,message = "速报标题长度必须为1~20")
    @TableField(value = "report_title")
    private String reportTitle;

    /**
     * 跳转类型。若不跳转则为null。
     */
    @EnumValidation(clazz = SkipLinkTypeEnum.class, message = "没有该跳转类型")
    @TableField(value = "skip_type")
    private String skipType;

    /**
     * 跳转链接
     */
    @TableField(value = "skip_link")
    private String skipLink;

    /**
     * 跳转链接的名称
     */
    @TableField(value = "skip_link_name")
    private String skipLinkName;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "date")
    private Date date;

    /**
     * 状态。0为已删除，1为正常。
     */
    @JsonIgnore
    @TableField(value = "status")
    private Integer status;

    private static final long serialVersionUID = 1L;

    public static final String COL_REPORT_ID = "report_id";

    public static final String COL_REPORT_TITLE = "report_title";

    public static final String COL_SKIP_TYPE = "skip_type";

    public static final String COL_SKIP_LINK = "skip_link";

    public static final String COL_SKIP_LINK_NAME = "skip_link_name";

    public static final String COL_DATE = "date";

    public static final String COL_STATUS = "status";
}