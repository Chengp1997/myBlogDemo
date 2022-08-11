package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gpchen.blog.dao.SysUserMapper;
import com.gpchen.blog.model.entity.SysUser;
import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.UserVo;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.SSOService;
import com.gpchen.blog.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    @Lazy
    private SSOService ssoService;

    /**
     * This method is used to find the author. if the author is empy, set a new anonymous one to it
     * @param id
     * @return
     */
    @Override
    public UserVo findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/avatar.png");
            sysUser.setNickname("anyOne");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);//column,value
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAvatar,SysUser::getNickname,SysUser::getAccount);//需要的列
        queryWrapper.last("limit 1");//加快匹配速度
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * find user by existing token
     * 1. validate parameters
     * @param token
     * @return
     */
    @Override
    public Result findUserByToken(String token) {
        SysUser sysUser = ssoService.checkToken(token);
        if(sysUser==null){
            return Result.fail(ErrorCode.TOKEN_INVALID.getCode(),ErrorCode.TOKEN_INVALID.getMsg());
        }
        //不为null就返回对应的user
        UserVo userVo = new UserVo();
        userVo.setId(sysUser.getId());
        userVo.setAccount(sysUser.getAccount());
        userVo.setNickname(sysUser.getNickname());
        userVo.setAvatar(sysUser.getAvatar());
        return Result.success(userVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //注意，保存的地方，id会自动生成，默认生成的id是分布式id---雪花算法生成的
        sysUserMapper.insert(sysUser);
    }

}
