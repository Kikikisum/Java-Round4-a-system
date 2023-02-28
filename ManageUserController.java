package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.server.Entity.SysUser;
import com.server.Mapper.SysUserMapper;
import com.server.Param.Result;
import com.server.Service.SysUserService;
import com.server.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class ManageUserController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;
    /**
     *用于查询一个用户
     * @param map 有token和一个用户的用户名
     * @return
     */
    @PostMapping("/info")
    public Result ret_Info(@RequestBody Map<String,String> map){
        SysUser user = sysUserService.getUserByLoginName(map.get("username"));
        user.setPassword("");
        return Result.succ(MapUtil.builder()
                .put("user",user)
                .map());
    }
    @PostMapping("/list")
    public Result retUserList(){
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.select(SysUser.class,info-> !info.getColumn().equals("password"));//排除掉密码
        List<SysUser> users = sysUserMapper.selectList(lqw1);
        return Result.succ(MapUtil.builder()
                .put("records",users)
                .map());
    }
    //修改一个用户信息
    @PostMapping("update")
    public Result updateUser(@RequestBody Map<String,String> map){
        SysUser user = new SysUser();
        user.setId(Integer.valueOf(map.get("id")));
        user.setName(map.get("username"));
        user.setNickname(map.get("nickname"));
        user.setIdentity(map.get("identity"));
        user.setEmail(map.get("email"));
        sysUserMapper.updateById(user);
        return Result.succ("用户信息修改成功");
    }
    //批量删除用户
    @PostMapping("/deletelist")
    public Result deleteList(@RequestBody Map<String,String> map){
        List<Integer> list= new ArrayList<>();
        for (String value : map.values()) {
            list.add(Integer.valueOf(value));
        }
        sysUserMapper.deleteBatchIds(list);
        return Result.succ("批量删除");
    }
    //重置用户密码
    @PostMapping("/resetpwd")
    public Result resetPwd(@RequestBody Map<String,String> map){
        SysUser user = new SysUser();
        user.setId(Integer.valueOf(map.get("id")));
        user.setPassword("88888888");
        sysUserMapper.updateById(user);
        return Result.succ("用户密码重置");
    }
    //修改用户密码
    @PostMapping("/updatetpwd")
    public Result updatePwd(@RequestBody Map<String,String> map){
        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getId,map.get("id"));
        lqw1.eq(SysUser::getPassword,map.get("oldPwd"));
        SysUser sysUser = sysUserMapper.selectOne(lqw1);
        if (!sysUser.getPassword().isEmpty())
        {
            sysUser.setPassword(map.get("newPwd"));
            sysUserMapper.updateById(sysUser);
            return Result.succ("修改密码成功");
        }
        else
            return Result.dataError("原密码错误");
    }
}
