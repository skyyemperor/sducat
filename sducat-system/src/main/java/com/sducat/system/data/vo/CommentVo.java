package com.sducat.system.data.vo;

import com.sducat.common.core.data.dto.UserDto;
import com.sducat.common.core.data.po.SysUser;
import com.sducat.system.data.dto.CatLessDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by skyyemperor on 2021-02-03 0:14
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 评论对应的猫咪ID
     */
    private CatLessDto cat;

    /**
     * 用户
     */
    private UserDto user;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片链接数组
     */
    private String pic;

    /**
     * 点赞数
     */
    private Integer like;

    /**
     * 发布时间
     */
    private LocalDateTime date;

    /**
     * 类型
     */
    private Integer status;

    /**
     * 用户是否赞过
     */
    private Boolean liked;


    private static final long serialVersionUID = 1L;
}
