package com.sducat.system.data.vo;

import com.sducat.common.core.data.dto.UserDto;
import com.sducat.system.data.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 通知的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVo implements Serializable {
    /**
     * 通知唯一ID
     */
    private Long noticeId;

    /**
     * 通知发往的用户
     */
    private Long userId;

    /**
     * 通知类型
     */
    private Integer noticeType;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 跳转类型
     */
    private String skipType;

    /**
     * 跳转链接
     */
    private String skipLink;

    /**
     * 相关用户(点赞的用户、回复评论的用户)
     */
    private UserDto relatedUser;

    /**
     * 相关评论内容
     */
    private CommentLessVo relatedComment;

    /**
     * 时间
     */
    private Date date;

    /**
     * 是否已读
     */
    private Integer read;

    private static final long serialVersionUID = 1L;
}
