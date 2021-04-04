package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "science_point")
public class SciencePoint implements Serializable {
    /**
     * 知识点ID
     */
    @TableId(value = "point_id", type = IdType.AUTO)
    private Long pointId;

    /**
     * 板块ID
     */
    @TableField(value = "module_id")
    private Long moduleId;

    /**
     * 知识点题目
     */
    @TableField(value = "title")
    private String title;

    /**
     * 知识点内容
     */
    @TableField(value = "content")
    private String content;

    public SciencePoint(Long moduleId, String title, String content) {
        this.moduleId = moduleId;
        this.title = title;
        this.content = content;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_POINT_ID = "point_id";

    public static final String COL_MODULE_ID = "module_id";

    public static final String COL_TITLE = "title";

    public static final String COL_CONTENT = "content";
}