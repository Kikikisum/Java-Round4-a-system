package com.server.Service.Impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.Entity.SysItem;
import com.server.Entity.SysUser;
import com.server.Mapper.SysItemMapper;
import com.server.Mapper.SysUserMapper;
import com.server.Service.SysItemService;
import com.server.Service.SysUserService;
import com.server.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    private SysUser user;

    @Override
    public List<SysUser> getAllUser() {
        return sysUserMapper.selectList(null);
    }


    //返回0是都正确
    //返回1是用户名错误
    //返回2是密码错误
    @Override
    public int checkUser(String loginName, String password) {
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getName,loginName);//筛选
        this.user = sysUserMapper.selectOne(lqw1);
        if (this.user==null)
            return 1;//返回1是用户名错误
        else if (user.getPassword().equals(password)){
            return 0;//返回0是都正确
        }else
            return 2;//返回2是密码错误
    }

    @Override
    public SysUser getUserByLoginName(String loginName) {
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getName,loginName);//筛选
        SysUser sysUser = sysUserMapper.selectOne(lqw1);
        return sysUser;
    }

    @Override
    public void insertUser(SysUser user) {
        sysUserMapper.insert(user);
    }

    @Override
    public boolean findUserName(String userName) {
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getName,userName);//筛选
        this.user = sysUserMapper.selectOne(lqw1);
        if(user!=null)
            return true;
        else
            return false;
    }
}
