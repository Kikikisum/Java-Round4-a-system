package com.server.Entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "userinformation")
public class SysUser {
    //数据都与数据库userinformation的数据一一对应
    private int id;
    private String name;
    private String password;
    private String email;
    private int status;
    private double money;
}
