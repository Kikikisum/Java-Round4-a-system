package com.server.Controller;

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
        String nickName = map.get("nickname");
        String password = map.get("password");
        String identity = map.get("identity");
        String email = map.get("email");

        //查找是否有相同用户名的用户

        if (!sysUserService.findUserName(registerName))
        {
            SysUser user = new SysUser();
            user.setName(registerName);
            user.setNickname(nickName);
            user.setPassword(password);
            user.setIdentity(identity);
            user.setEmail(email);
            user.setStatus(1);
            sysUserService.insertUser(user);
            return Result.registerSucc();
        }
        return Result.registerFail();
    }
}
