package com.gpchen.blog.handler;

import com.alibaba.fastjson.JSON;
import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.SSOService;
import com.gpchen.blog.util.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {//The interceptor for all resources that needed to log in

    @Autowired
    private SSOService ssoService;

    /**
     * before go to controller, preHandle will trigger first
     * condition:
     * 1. the api path is a method of controller--if only search for resources, no need to intercept
     * 2. if current token is empty-- not log in--need to log in, goes to the login page
     * 3. if current token is not empty, need to verify the token.
     * 4. if token is verified, can release the resource
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //check the path -> not handler method(not controller method) let go.
        if(! (handler instanceof HandlerMethod))return true;
        //check token
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        //if token is empty -> not log in, need to log in
        if(StringUtils.isBlank(token)||token.equals("undefined")){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        //if token is not empty, need to verify the token, whether the user exist
        SysUser sysUser = ssoService.checkToken(token);
        if(sysUser==null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        //put current user into ThreadLocal, so that we can get current user in a more easy way
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
