package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by skyyemperor on 2021-02-07 21:57
 * Description : 
 */ 
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cat_relation")
public class CatRelation implements Serializable {
    @TableId(value = "cat_id", type = IdType.INPUT)
    private Long catId;

    @TableField(value = "relative_id")
    private Long relativeId;

    private static final long serialVersionUID = 1L;

    public static final String COL_CAT_ID = "cat_id";

    public static final String COL_RELATIVE_ID = "relative_id";
}