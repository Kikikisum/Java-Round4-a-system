package com.server.Interceptor;


import com.alibaba.fastjson.JSONObject;
import com.server.Param.ApiResponse;
import com.server.Utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//Token拦截器
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
//        System.out.println(response);
        String token = request.getHeader("token");
        System.out.println("需验证的："+token);
//        System.out.println(token.getClass());
        //判断token是否存在
        if (!token.equals("")){
            //验证token是否正确
            if (JwtUtils.verify(token))
                System.out.println("token验证成功");
            return true;
        }
        System.out.println("验证失败");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 返回信息给客户端
     * @param response
     * @param out
     * @param apiResponse
     */
    private void responseMessage(HttpServletResponse response, PrintWriter out, ApiResponse apiResponse){
        response.setContentType("application/json; charset=utf-8");
        out.write(JSONObject.toJSONString(apiResponse));
        out.flush();
        out.close();

    }
}
