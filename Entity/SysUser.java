package com.server.Entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "userinformation")
public class SysUser {
    //数据都与数据库userinformation的数据一一对应
    private int id;
    private String name;
    private String nickname;
    private String password; //密码
    private String identity; //身份
    private String email; //联系方式
    private int status;

}
