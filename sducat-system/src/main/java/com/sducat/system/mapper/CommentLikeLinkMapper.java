package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.CommentLikeLink;
import org.apache.ibatis.annotations.Param;

/**
 * Created by skyyemperor on 2021-02-07 20:27
 * Description :
 */
public interface CommentLikeLinkMapper extends BaseMapper<CommentLikeLink> {

    CommentLikeLink judge(@Param("userId") Long userId,
                          @Param("commentId") Long commentId);

    Integer likeComment(@Param("userId") Long userId,
                        @Param("commentId") Long commentId);


    Integer unLikeComment(@Param("userId") Long userId,
                          @Param("commentId") Long commentId);


}