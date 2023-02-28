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

    @Override
    public boolean checkUser(String loginName, String password) {
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getName,loginName);//筛选
        this.user = sysUserMapper.selectOne(lqw1);
        if (user.getName().equals(loginName)&&user.getPassword().equals(password)){
            return true;
        }else
            return false;
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
        lqw1.eq(SysUser::getStatus,1);//筛选删除
        lqw1.eq(SysUser::getName,userName);//筛选
        this.user = sysUserMapper.selectOne(lqw1);
        if(user!=null)
            return true;
        else
            return false;
    }
}
