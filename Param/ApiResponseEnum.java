package com.server.Param;

public enum ApiResponseEnum {
    /**
     * API调用成功返回
     */
    SUCCESS(10000,"请求成功"),
    FAIL(10001,"请求失败"),
    VERIFY_ERROR(10002,"验证码错误"),
    LOGIN_FAIL(10099,"登陆失败"),
    AUTH_ERROR(10100,"认证失败");



    private int Code = 0;

    private String messages;

    private ApiResponseEnum(int Code, String messages) {
        this.Code = Code;
        this.messages = messages;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return messages;
    }

    public void setMsg(String messages) {
        this.messages = messages;
    }
}
