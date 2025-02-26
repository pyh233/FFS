package com.example.flyfishshop.config;

import com.example.flyfishshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
// 在配置项目中添加允许缓存 可以使用AOP缓存Service层.
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableTransactionManagement
public class MyConfig implements WebMvcConfigurer {
    OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminUserInterceptor())
                .addPathPatterns("/admin/**/*")
                .excludePathPatterns("/admin/login/**",
                        "/admin/captcha/**",
                        "/admin/register/**",
                        "/admin/forgot-password/**",
                        "/admin/api/v1/upload/image/**");//NOTE:图片上传前后台都使用，因此排除
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/cart/**", "/order/**", "/my/**");
        registry.addInterceptor(new FrontHeadBarMsgInterceptor(orderService))
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**/*");
    }

    // 自定义Service缓存Key生成器
    // @Bean让Spring创建唯一的实例
    @Bean("myKeyGenerator")
    public KeyGenerator MyConfig() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName() + params.length + Arrays.toString(params) + Arrays.deepToString(params);
            }
        };
    }
}
