package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.SSOParams;
import com.gpchen.blog.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    //这里如果直接使用SysUserService不好，SysUserService应该理解为专门和后端SysUser表格进行交互的服务，因此要换一个具体的功能类更好
    @Autowired
    private SSOService SSOService;

    @PostMapping
    @LogAnnotation(module="login", operation="login")
    public Result login(@RequestBody SSOParams SSOParams){
        return SSOService.login(SSOParams);
    }
}
