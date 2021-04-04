package com.sducat.system.data.po.notice;

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
 * Created by skyyemperor on 2021-02-25
 * Description : 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "notice")
public class Notice implements Serializable {
    /**
     * 通知唯一ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    /**
     * 通知发往的用户
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 通知类型
     */
    @TableField(value = "notice_type")
    private Integer noticeType;

    /**
     * 通知标题
     */
    @TableField(value = "`title`")
    private String title;

    /**
     * 通知内容
     */
    @TableField(value = "`content`")
    private String content;

    /**
     * 跳转类型
     */
    @TableField(value = "skip_type")
    private String skipType;

    /**
     * 跳转链接
     */
    @TableField(value = "skip_link")
    private String skipLink;

    /**
     * 相关用户ID(点赞的用户、回复评论的用户)
     */
    @TableField(value = "related_user_id")
    private Long relatedUserId;

    /**
     * 相关评论内容
     */
    @TableField(value = "related_comment_id")
    private Long relatedCommentId;

    /**
     * 时间
     */
    @TableField(value = "`date`")
    private Date date;

    /**
     * 是否已读
     */
    @TableField(value = "`read`")
    private Integer read;

    private static final long serialVersionUID = 1L;

}