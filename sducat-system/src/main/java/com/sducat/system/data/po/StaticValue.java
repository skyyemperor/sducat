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
 * Created by skyyemperor on 2021-03-09
 * Description : 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "static_value")
public class StaticValue implements Serializable {
    @TableField(value = "`key`")
    private String key;

    @TableField(value = "`value`")
    private String value;

    private static final long serialVersionUID = 1L;

    public static final String COL_KEY = "key";

    public static final String COL_VALUE = "value";
}