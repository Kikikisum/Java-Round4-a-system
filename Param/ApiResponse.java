package com.server.Param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {
    private int Code;
    private String messages;
    private Object data;
    public ApiResponse(ApiResponseEnum apiResponseEnum){
        this.Code = apiResponseEnum.getCode();
        this.messages = apiResponseEnum.getMsg();
    }
    public ApiResponse(Object data) {
        this.data = data;
    }
}
