package com.gpchen.blog.service;

import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.SSOParams;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SSOService {
    Result login(SSOParams SSOParams);

    Result logout(String token);

    Result register(SSOParams SSOParams);

    SysUser checkToken(String token);
}
