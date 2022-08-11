package com.gpchen.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Slf4j
@Component
public class CacheAspect {

    //we used redis as cache db
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Pointcut("@annotation(com.gpchen.blog.common.cache.Cache)")
    public void cachePointcut(){}

    @Around("cachePointcut()")
    public Object around(ProceedingJoinPoint point){
        try {
            //used to log
            //class name
            String className = point.getTarget().getClass().getSimpleName();
            //method name
            String methodName = point.getSignature().getName();

            //arguments we are going to store
            Class[] parameterTypes = new Class[point.getArgs().length];
            Object[] args = point.getArgs();
            //for all parameters,store all the parameters using json and connect them; store all the types of arguments
            String params = "";
            for (int i=0;i<args.length;i++){
                if (args[i]!=null){
                    params+= JSON.toJSONString(args[i]);
                    parameterTypes[i]=args[i].getClass();
                }else{
                    parameterTypes[i]=null;
                }
            }
            //hash the key, in case keys are too long that unable to fetch from db
            if(StringUtils.isNotEmpty(params)){
                params= DigestUtils.md5Hex(params);
            }

            //current method
            Method method = point.getSignature().getDeclaringType().getMethod(methodName,parameterTypes);
            //get annotation info
            Cache annotation = method.getAnnotation(Cache.class);
            //stating storing keys and value
            long expire = annotation.expire();
            String name = annotation.name();
            String key = name+"::"+className+"::"+methodName+"::"+params;
            //check if already in redis
            String value = redisTemplate.opsForValue().get(key);
            if(StringUtils.isNotEmpty(value)){
                log.info("=====================get from redis================================\n");
                log.info("get from the redis,{},{}",className,methodName);
                log.info("=====================get from redis================================\n");
                return JSON.parseObject(value,Result.class);
            }
            //if not in redis, store them in redis
            Object proceed = point.proceed();
            redisTemplate.opsForValue().set(key,new ObjectMapper().writeValueAsString(proceed), Duration.ofMillis(expire));
            log.info("=====================save to redis================================\n");
            log.info("save in redis,{},{}",className,methodName);
            log.info("=====================save to redis================================\n");
            return proceed;
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return Result.fail(ErrorCode.SYSTEM_ERROR.getCode(),ErrorCode.SYSTEM_ERROR.getMsg());
    }
}
