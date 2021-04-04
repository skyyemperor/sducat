package com.sducat.system.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by skyyemperor on 2021-03-02
 * Description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentLessVo implements Serializable {
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
    private String pic;

    private static final long serialVersionUID = 1L;
}
