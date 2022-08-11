package com.gpchen.blog.controller;

import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {
    @Autowired
    private SSOService SSOService;

    @GetMapping
    @LogAnnotation(module="logout", operation="logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return SSOService.logout(token);
    }
}
