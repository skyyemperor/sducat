package com.sducat.system.data.dto;

import com.sducat.common.util.JSONUtil;
import com.sducat.system.data.vo.CommentLessVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by skyyemperor on 2021-03-02
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentLessDto {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片链接数组
     */
    private List<String> pic;

    public static CommentLessDto getDto(CommentLessVo vo) {
        if (vo == null) return null;
        return new CommentLessDto(
                vo.getCommentId(),
                vo.getContent(),
                JSONUtil.getStringArray(vo.getPic())
        );
    }

}
