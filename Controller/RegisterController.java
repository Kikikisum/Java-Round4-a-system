package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import com.server.Entity.SysUser;
import com.server.Param.Result;
import com.server.Service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    SysUserService sysUserService;

    @PostMapping("/register")
    public Result registerUser(@RequestBody Map<String,String> map)
    {
        String registerName = map.get("username");
        String password = map.get("password");
        String email = map.get("email");
        if (registerName==""||password=="")
            return Result.retInfo(10007,"用户密码账号不能为空", MapUtil.builder().map());
        if (email=="")
            return Result.retInfo(10010,"邮箱不能为空",MapUtil.builder().map());
        //查找是否有相同用户名的用户
        if (!sysUserService.findUserName(registerName))
        {
            SysUser user = new SysUser();
            user.setName(registerName);
            user.setPassword(password);
            user.setEmail(email);
            user.setMoney(0);
            user.setStatus(1);
            sysUserService.insertUser(user);
            return Result.registerSucc();
        }else
            return  Result.retInfo(10006,"用户已存在",MapUtil.builder().map());
    }
}
