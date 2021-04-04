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
 * Created by skyyemperor on 2021-02-15 19:48
 * Description : 
 */ 
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cat_feedback")
public class Feedback implements Serializable {
    /**
     * 反馈ID
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedbackId;

    /**
     * 猫咪ID
     */
    @TableField(value = "cat_id")
    private Long catId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 反馈内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 联系方式
     */
    @TableField(value = "contact_way")
    private String contactWay;

    /**
     * 反馈时间
     */
    @TableField(value = "date")
    private Date date;

    /**
     * 是否已审核
     */
    @TableField(value = "`checked`")
    private Integer checked;

    public Feedback(Long catId, Long userId, String content, String contactWay, Date date,Integer checked) {
        this.catId = catId;
        this.userId = userId;
        this.content = content;
        this.contactWay = contactWay;
        this.date = date;
        this.checked = checked;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_FEEDBACK_ID = "feedback_id";

    public static final String COL_CAT_ID = "cat_id";

    public static final String COL_CONTENT = "content";

    public static final String COL_CONTACT_WAY = "contact_way";

    public static final String COL_DATE = "date";

}