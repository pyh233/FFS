package com.example.flyfishshop.config;

import com.example.flyfishshop.model.Order;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class FrontHeadBarMsgInterceptor implements HandlerInterceptor {
    OrderService orderService;

    public FrontHeadBarMsgInterceptor(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(UserLoginInterceptor.USER_LOGIN_IDENTIFY);
        // 拦截器获取会话中的用户信息
        if (user != null) {
            //NOTE:为什么要存储而无法直接从session中取出用户呢?
            request.setAttribute("user", user);
            // 查询出当前用户最新的一笔未支付的订单
            Order notPayedOrder = orderService.getNotPayedOrder(user.getId());
            request.setAttribute("notPayedOrder", notPayedOrder);
        }

        return true;
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
