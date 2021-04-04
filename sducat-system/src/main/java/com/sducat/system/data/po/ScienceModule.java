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
@TableName(value = "science_module")
public class ScienceModule implements Serializable {
    /**
     * 科普板块ID
     */
    @TableId(value = "module_id", type = IdType.AUTO)
    private Long moduleId;

    /**
     * 科普板块名称
     */
    @TableField(value = "module_name")
    private String moduleName;

    /**
     * 板块背景图
     */
    @TableField(value = "module_main_pic")
    private String moduleMainPic;

    /**
     * 板块背景图
     */
    @TableField(value = "module_slide_pic")
    private String moduleSlidePic;

    public ScienceModule(String moduleName, String moduleMainPic, String moduleSlidePic) {
        this.moduleName = moduleName;
        this.moduleMainPic = moduleMainPic;
        this.moduleSlidePic = moduleSlidePic;
    }

    private static final long serialVersionUID = 1L;
}