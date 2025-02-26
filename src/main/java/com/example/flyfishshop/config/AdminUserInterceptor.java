package com.example.flyfishshop.config;

import com.example.flyfishshop.model.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


// 定义一个拦截器 写下拦截条件以及跳转方向
public class AdminUserInterceptor implements HandlerInterceptor {
    public static final String LOGIN_ADMIN_IDENTIFIED = "@#CURRENT_LOGIN_ADMIN_IDENTIFIED";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute(LOGIN_ADMIN_IDENTIFIED);
        if (admin == null) {
            String header = request.getHeader("X-requested-With");
            if(header != null && header.equalsIgnoreCase("XMLHttpRequest")) {
                // 如果是ajax请求，发送401状态码,让ajax跳转
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }else {// 如果不是ajax请求直接重定向到登录
                response.sendRedirect(request.getContextPath() + "/admin/login");
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
