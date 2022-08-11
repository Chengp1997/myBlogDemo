package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    @LogAnnotation(module="users", operation="get current User by token")
    @Cache(expire = 5 * 60 * 1000,name = "get_current_user_by_token")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }
}
