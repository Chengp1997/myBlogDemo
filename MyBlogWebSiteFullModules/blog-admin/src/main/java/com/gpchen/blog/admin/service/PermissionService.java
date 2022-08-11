package com.gpchen.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpchen.blog.admin.dao.PermissionMapper;
import com.gpchen.blog.admin.model.entity.Permission;
import com.gpchen.blog.admin.model.params.PageParams;
import com.gpchen.blog.admin.model.vo.PageResult;
import com.gpchen.blog.admin.model.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public Result listPermission(PageParams pageParams) {
        Page<Permission> page = new Page<>(pageParams.getCurrentPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParams.getQueryString())) {
            queryWrapper.eq(Permission::getName,pageParams.getQueryString());
        }
        Page<Permission> permissionPage = this.permissionMapper.selectPage(page,queryWrapper);
        PageResult<Permission> pageResult = new PageResult<>();
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return Result.success(pageResult);
    }

    public Result addPermission(Permission permission) {
        permissionMapper.insert(permission);
        return  Result.success(null);
    }

    public Result updatePermission(Permission permission) {
        permissionMapper.updateById(permission);
        return  Result.success(null);
    }

    public Result deletePermission(Long id) {
        permissionMapper.deleteById(id);
        return Result.success(null);
    }
}
