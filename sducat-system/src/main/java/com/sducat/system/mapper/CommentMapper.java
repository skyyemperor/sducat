package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.common.enums.CommentListTypeEnum;
import com.sducat.system.data.dto.CommentDto;
import com.sducat.system.data.po.Comment;
import com.sducat.system.data.vo.CommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * Created by skyyemperor on 2021-02-06 21:26
 * Description :
 */
public interface CommentMapper extends BaseMapper<Comment> {
    CommentVo selectCommentByCommentId(@Param("userId") Long userId,
                                       @Param("commentId") Long commentId);

    List<CommentVo> selectCommentList(@Param("userId") Long userId,
                                      @Param("catId") Long catId,
                                      @Param("status") Integer status,
                                      @Param("offset") Integer offset,
                                      @Param("count") Integer count,
                                      @Param("type") CommentListTypeEnum type);

    List<CommentVo> selectPersonalCommentList(@Param("userId") Long userId,
                                              @Param("offset") Integer offset,
                                              @Param("count") Integer count);

    List<CommentVo> selectLikedCommentList(@Param("userId") Long userId,
                                           @Param("offset") Integer offset,
                                           @Param("count") Integer count);

    Integer countCommentList(@Param("catId") Long catId,
                             @Param("status") Integer status);

    List<CommentVo> selectNoCheckComment(@Param("userId") Long userId,
                                         @Param("offset") Integer offset,
                                         @Param("count") Integer count);

    int checkComment(@Param("commentId") Long commentId,
                     @Param("pass") boolean pass);
}