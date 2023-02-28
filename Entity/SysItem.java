package com.server.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "iteminformation")
public class SysItem {
    private int id; //项目编号
    private String identity; //众筹者身份
    private int user_id; //众筹者
    private int audit_status; //审核情况
    private int price; //筹资情况
    private String title; //项目标题
    private String content; //项目简介
    @TableField("imageurl")
    private String imageurl; //图片材料
    private int status; //图片上传的状态
}
