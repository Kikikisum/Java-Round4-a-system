package com.server.Param;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;
    public static Result succ(Object data) {
        return retInfo(10000, "操作成功", data);
    }
    public static Result error() {
        return retInfo(10001, "操作失败", null);
    }
    public static Result dataError(Object data) {
        return retInfo(10001, "操作失败", data);
    }
    public static Result verifyError(){
        return retInfo(10002,"验证码错误",null);
    }
    public static Result loginFail(){
        return retInfo(10003,"登录失败",null);
    }
    public static Result registerFail(){
        return retInfo(10004,"注册失败",null);
    }
    public static Result registerSucc(){
        return retInfo(10005,"注册成功",null);
    }
    public static Result pwdFail(){
        return retInfo(10006,"密码错误",null);
    }
    public static Result retMsg(int code, String msg){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
    public static Result retData(Object data){
        Result r = new Result();
        r.setData(data);
        return r;
    }
    public static Result retInfo(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
