package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.server.Entity.SysItem;
import com.server.Entity.SysUser;
import com.server.Mapper.SysItemMapper;
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

    @Autowired
    SysItemMapper sysItemMapper;
    /**
     *用于查询一个用户
     * @param map 有token和一个用户的用户名
     * @return
     */
    @PostMapping("/info")
    public Result ret_Info(@RequestBody Map<String,String> map){
        if (map.get("username")=="")
        {
            SysUser user = sysUserService.getUserByLoginName(map.get("username"));
            user.setPassword("");
            return Result.succ(MapUtil.builder()
                    .put("user",user)
                    .map());
        }
        return Result.retInfo(10003,"用户名不能为空",MapUtil.builder().map());

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
        user.setEmail(map.get("email"));
        user.setMoney(Double.parseDouble(map.get("money")));
        user.setPassword(map.get("password"));
        user.setStatus(Integer.parseInt(map.get("status")));
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
        if (list.size()!=0)
        {
            sysUserMapper.deleteBatchIds(list);
            return Result.succ("批量删除");
        }
        else
            return Result.retInfo(10004,"删除用户数目为0",MapUtil.builder().map());

    }
    @PostMapping("/contribution")
    public Result conItem(@RequestBody Map<String,String> map){

        LambdaQueryWrapper<SysUser> lqw1 = new LambdaQueryWrapper<SysUser>();
        lqw1.eq(SysUser::getId,map.get("id"));//获取用户id
        SysUser sysUser = sysUserMapper.selectOne(lqw1);
        if (sysUser.getMoney() - Double.parseDouble(map.get("money"))<0)
            return Result.retInfo(10015,"用户余额不足",MapUtil.builder().map());
        else
        {
            double money = sysUser.getMoney() - Double.parseDouble(map.get("money"));
            sysUser.setMoney(money);
            LambdaQueryWrapper<SysItem> lqw2 = new LambdaQueryWrapper<SysItem>();
            lqw2.eq(SysItem::getId,map.get("itemId"));//获取用户id
            SysItem sysItem =sysItemMapper.selectOne(lqw2);
            sysItem.setPrice(sysItem.getPrice()+Double.parseDouble(map.get("money")));
            sysUserMapper.updateById(sysUser);
            sysItemMapper.updateById(sysItem);
            return Result.succ("出资成功");
        }
    }
}
