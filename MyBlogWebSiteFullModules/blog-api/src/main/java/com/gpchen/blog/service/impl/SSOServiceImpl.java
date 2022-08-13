package com.gpchen.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.SSOParams;
import com.gpchen.blog.service.SSOService;
import com.gpchen.blog.service.SysUserService;
import com.gpchen.blog.util.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SSOServiceImpl implements SSOService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //加密盐，给密码加密用
    private static final String salt = "gpchen!@#$%";

    /**
     * for login
     * 1. validate parameters
     * 2. find user in db, check exist or not
     *    2.1 not exist, login fail
     *    2.2 exist, JWT generate token
     * 3. store token into redis -- fast
     * @param SSOParams
     * @return
     */
    @Override
    public Result login(SSOParams SSOParams) {
        String account = SSOParams.getAccount();
        String password = SSOParams.getPassword();
        //validate parameters
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode() ,ErrorCode.PARAMS_ERROR.getMsg());
        }

        password= DigestUtils.md5Hex(password+salt);
        //hash the password and search db
        SysUser sysUser = sysUserService.findUser(account,password);
        if(sysUser==null) return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        //user exist, generate token
        String token = JWTUtils.createToken(sysUser.getId());
        //store token into redis
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    /**
     * for logout
     * only need to delete the token in the redis
     * @param token the token that is ready to be deleted
     * @return
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    /**
     * for registration
     * 1. validate parameters
     * 2. search whether account exist or not
     * 3. not exist, register new one, and login
     * 4. exist, login
     * @param SSOParams
     * @return
     */
    @Override
    public Result register(SSOParams SSOParams) {
        String account = SSOParams.getAccount();
        String password = SSOParams.getPassword();
        String nickname = SSOParams.getNickname();

        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser!=null){//存在，直接返回
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //create new one
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));//存的是加密后的
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/avatar.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        //store token in redis
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }

    /**
     * Check the token to find the user
     * 1. if token is blank, user is null
     * 2. check if it is a valid token or not
     * 3. check if it is already in redis
     * 4. if not in redis, not login now,return null boject
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {
        //empty or not
        if(StringUtils.isBlank(token)||token==null){
            return null;
        }
        //can be parsed
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){//can not resolve
            return null;
        }
        //是否在redis
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        return JSON.parseObject(userJson,SysUser.class);
    }

}
