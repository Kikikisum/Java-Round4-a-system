package com.server.Utils;

import com.server.Param.ApiResponse;
import com.server.Param.ApiResponseEnum;

public class ApiResponseUtil {
    /**
     * 获取请求成功响应的ApiResponse
     */
    public static ApiResponse getApiResponse(Object data){
        return getApiResponse(data, ApiResponseEnum.SUCCESS.getCode(), ApiResponseEnum.SUCCESS.getMsg());
    }

    /**
     * 获取其他请求响应的ApiResponse
     * @param message
     * @param code
     * @return
     */
    public static ApiResponse getApiResponse(int code,String message){
        return getApiResponse(null,code,message);
    }

    public static ApiResponse getApiResponse(ApiResponseEnum apiResponseEnum){
        return getApiResponse(apiResponseEnum.getCode(),apiResponseEnum.getMsg());

    }

    public static ApiResponse getApiResponse(Object data, int code, String msg) {
        ApiResponse apiResponse = new ApiResponse(data);
        apiResponse.setCode(code);
        apiResponse.setMessages(msg);
        return apiResponse;
    }
}
