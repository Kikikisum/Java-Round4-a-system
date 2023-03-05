package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import com.server.Entity.SysUser;
import com.server.Param.Result;
import com.server.Service.SysUserService;
import com.server.Utils.JwtUtils;
import com.server.Utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    SysUserService sysUserService;

    private String code="";//验证码

    //登录认证
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map)
    {
        String loginName = map.get("username");
        String password = map.get("password");
        if (loginName=="")
            return Result.retInfo(100001,"账号不能为空",MapUtil.builder().map());
        else if(password=="")
            return Result.retInfo(100002,"密码不能为空",MapUtil.builder().map());
        int flag = sysUserService.checkUser(loginName,password);
        //身份验证
        if (flag==0){
            SysUser user = sysUserService.getUserByLoginName(loginName);
            if (user != null){
                //返回token值
                String token = JwtUtils.sign(user.getName(),user.getId());
                if (token != null){
                    //返回成功信息
                    user.setPassword("");
                    return Result.succ(MapUtil.builder()
                            .put("token",token).put("user",user)
                            .map());
                }
            }
        }else if (flag==1)//用户名错误
            return Result.retInfo(100003,"用户名不存在",MapUtil.builder().map());
        else if (flag==2) //密码错误
            return Result.retInfo(100004,"密码错误",MapUtil.builder().map());
        return null;
    }

    //验证码发送
    @GetMapping("/captcha")
    public void getVerifyCodeImg(HttpServletResponse response, HttpSession session){

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        code = VerifyCodeUtil.drawImage(output);
        //将验证码文本直接存放到session中
        session.setAttribute("verifyCode", code);
        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
