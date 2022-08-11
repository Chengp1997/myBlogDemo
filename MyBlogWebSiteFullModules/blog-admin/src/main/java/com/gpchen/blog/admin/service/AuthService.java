package com.gpchen.blog.admin.service;

import com.gpchen.blog.admin.model.entity.Admin;
import com.gpchen.blog.admin.model.entity.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;
    public boolean auth(HttpServletRequest httpServletRequest, Authentication authentication){
        //权限认证。
        //当前的请求路径
        String requestURI = httpServletRequest.getRequestURI();
        //当前的登录用户信息
        Object principal = authentication.getPrincipal();
        if(principal==null||"anonymousUser".equals(principal)){
            //未登录
            return false;
        }
        //登录了
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin  = adminService.findAdminByUsername(username);
        if(admin==null){
            return false;
        }
        //如果是超级管理员，直接放行
        if(admin.getId()==1){
            return true;
        }
        Long adminId = admin.getId();
        List<Permission> permissionList = this.adminService.findPermissionByAdminId(adminId);
        requestURI = StringUtils.split(requestURI,'?')[0];//可能带参数，只要第一个
        for(Permission permission:permissionList){
            if(requestURI.equals(permission.getPath())){
                return true;
            }
        }
        return false;
    }
}
