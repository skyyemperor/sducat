package com.sducat.system.data.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sducat.common.constant.Constants;
import com.sducat.common.core.data.dto.UserDto;
import com.sducat.common.enums.CommentCheckTypeEnum;
import com.sducat.common.enums.CommentStatusEnum;
import com.sducat.system.data.vo.CommentVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by skyyemperor on 2021-02-02 23:44
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 父评论ID,暂时不实现
     */
//    private Long parentId;

    /**
     * 评论对应的猫咪ID
     */
    private CatLessDto cat;

    /**
     * 用户
     */
    private UserDto poster;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片链接数组
     */
    private List<String> pic;

    /**
     * 点赞数
     */
    private Integer like;

    /**
     * 发布时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String status;

    /**
     * 用户是否赞过
     */
    private Integer liked;

    private Integer checked;


    private static final long serialVersionUID = 1L;

    public static CommentDto getDto(CommentVo vo) {
        return new CommentDto(
                vo.getCommentId(),
//                vo.getParentId(),
                vo.getCat(),
                vo.getUser(),
                vo.getContent(),
                JSON.parseArray(vo.getPic(), String.class),
                vo.getLike(),
                vo.getDate(),
                CommentStatusEnum.getRemark(Math.abs(vo.getStatus())),
                vo.getLiked() == null ? null : (vo.getLiked() ? 1 : 0),
                vo.getStatus() == 0 ? -1 : (vo.getStatus() > 0 ? Constants.CHECK_SUCCESS : Constants.WAIT_CHECK)
        );
    }
}
