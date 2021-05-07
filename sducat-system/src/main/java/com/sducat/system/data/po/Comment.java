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
 * Created by skyyemperor on 2021-02-06 21:26
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`comment`")
public class Comment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 父评论ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 评论对应的猫咪ID
     */
    @TableField(value = "cat_id")
    private Long catId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 图片链接数组
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 点赞数
     */
    @TableField(value = "`like`")
    private Integer like;

    /**
     * 发布时间
     */
    @TableField(value = "`date`")
    private Date date;

    /**
     * 类型，0违规，1猫谱，2社区，3猫谱和社区，未审核的评论为负数
     */
    @TableField(value = "`status`")
    private Integer status;

    private static final long serialVersionUID = 1L;

    public static final String COL_COMMENT_ID = "comment_id";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_CAT_ID = "cat_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_CONTENT = "content";

    public static final String COL_PIC = "pic";

    public static final String COL_LIKE = "`like`";

    public static final String COL_DATE = "`date`";

    public static final String COL_STATUS = "`status`";
}