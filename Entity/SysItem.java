package com.server.Entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "iteminformation")
public class SysItem {
    private int id;
//    @TableField("user_id")
    private int userid;
    private String telephone;


    private double price;
    @TableField(value = "title",condition = SqlCondition.LIKE)
    private String title;
    private String content;
    @TableField("imageurl")
    private String imageurl;
    private int status;
}
