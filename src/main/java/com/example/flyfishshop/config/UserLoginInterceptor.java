package com.example.flyfishshop.config;

import com.example.flyfishshop.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserLoginInterceptor implements HandlerInterceptor {
    public static final String USER_LOGIN_IDENTIFY = "@USER_LOGIN_IDENTIFY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_IDENTIFY);
        // 拦截器获取会话中的用户信息
        if (user == null) {
            // 判断是否是ajax?分开控制
            String header = request.getHeader("x-requested-with");
            if(header != null && header.equalsIgnoreCase("XMLHttpRequest")) {
                //  如果是ajax请求 返回给ajax错误信息，让ajax控制跳转
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }else{
                // 如果不是ajax请求直接跳转到登录页面
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
