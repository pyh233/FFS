package com.example.flyfishshop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeStartAspect {
    @Pointcut("execution(* com.example.flyfishshop.service.*.*(..))")
    public void pointCut() {
        System.out.println("执行了切点");
    }
    @Before("pointCut()")
    public void before() {
        System.out.println("前置通切入");
    }
    @AfterThrowing("pointCut()")
    public void afterThrowing() {
        System.out.println("异常后通知");
    }
    @AfterReturning("pointCut()")
    public void afterReturning() {
        System.out.println("返回后通知");
    }
    @After("pointCut()")
    public void after() {
        System.out.println("后置切入");
    }
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");

        Object obj = jp.proceed();

        System.out.println("环绕后");
        return obj;
    }
}
