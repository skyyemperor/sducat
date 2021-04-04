package com.sducat.system.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
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
@TableName(value = "donation")
public class Donation implements Serializable {
    /**
     * 捐款唯一ID
     */
    @TableId(value = "donation_id", type = IdType.AUTO)
    private Long donationId;

    /**
     * 捐款人名称
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 捐款金额
     */
    @TableField(value = "money")
    private Integer money;

    /**
     * 捐款日期
     */
    @TableField(value = "`date`")
    private LocalDate date;

    public Donation(String userName, Integer money, LocalDate date) {
        this.userName = userName;
        this.money = money;
        this.date = date;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_DONATION_ID = "donation_id";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_MONEY = "money";

    public static final String COL_DATE = "date";
}