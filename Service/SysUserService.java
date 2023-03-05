package com.server.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.Entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    public List<SysUser> getAllUser();//获取全部用户
    public int checkUser(String loginName, String password);//登录验证
    SysUser getUserByLoginName(String loginName);//根据用户名获取用户信息
    public void insertUser(SysUser user);//插入一个用户信息
    public boolean findUserName(String userName);//查找是否有重复的用户名
}
