package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by skyyemperor on 2021-02-28
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "new_cat")
public class NewCat implements Serializable {
    /**
     * 新猫ID
     */
    @TableId(value = "new_cat_id", type = IdType.AUTO)
    private Long newCatId;

    /**
     * 提交的用户的ID
     */
    @TableField(value = "submit_user_id")
    private Long submitUserId;

    /**
     * 校区
     */
    @TableField(value = "campus")
    private String campus;

    /**
     * 目击地点
     */
    @TableField(value = "witness_location")
    private String witnessLocation;

    /**
     * 目击时间
     */
    @TableField(value = "witness_time")
    private String witnessTime;

    /**
     * 猫咪详情
     */
    @TableField(value = "description")
    private String description;

    /**
     * 图片链接数组
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 提交时间
     */
    @TableField(value = "submit_time")
    private Date submitTime;

    /**
     * 是否已审核
     */
    @TableField(value = "`checked`")
    private Integer checked;

    public NewCat(Long submitUserId, String campus, String witnessLocation, String witnessTime,
                  String description, String pic, Date submitTime, Integer checked) {
        this.submitUserId = submitUserId;
        this.campus = campus;
        this.witnessLocation = witnessLocation;
        this.witnessTime = witnessTime;
        this.description = description;
        this.pic = pic;
        this.submitTime = submitTime;
        this.checked = checked;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_NEW_CAT_ID = "new_cat_id";

    public static final String COL_SUBMIT_USER_ID = "submit_user_id";

    public static final String COL_CAMPUS = "campus";

    public static final String COL_WITNESS_LOCATION = "witness_location";

    public static final String COL_WITNESS_TIME = "witness_time";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_PIC = "pic";

    public static final String COL_SUBMIT_TIME = "submit_time";

    public static final String COL_CHECKED = "checked";
}