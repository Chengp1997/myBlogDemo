package com.gpchen.blog.service;

import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.UserVo;

public interface SysUserService {
    UserVo findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);
}
