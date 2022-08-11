package com.gpchen.blog.controller;

import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.SSOParams;
import com.gpchen.blog.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private SSOService SSOService;

    @PostMapping
    @LogAnnotation(module="register", operation="register")
    public Result register(@RequestBody SSOParams SSOParams){
        return SSOService.register(SSOParams);
    }
}
