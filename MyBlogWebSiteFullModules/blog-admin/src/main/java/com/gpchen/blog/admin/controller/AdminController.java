package com.gpchen.blog.admin.controller;

import com.gpchen.blog.admin.model.entity.Permission;
import com.gpchen.blog.admin.model.params.PageParams;
import com.gpchen.blog.admin.model.vo.Result;
import com.gpchen.blog.admin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission/permissionList")
    public Result listPermission(@RequestBody PageParams pageParams){
        return permissionService.listPermission(pageParams);
    }

    @PostMapping("/permission/add")
    public Result permissionAdd(@RequestBody Permission permission){
        return permissionService.addPermission(permission);
    }

    @PostMapping("/permission/update")
    public Result permissionUpdate(@RequestBody Permission permission){
        return permissionService.updatePermission(permission);
    }

    @GetMapping("/permission/delete/{id}")
    public Result permissionDelete(@PathVariable("id") Long id){
        return permissionService.deletePermission(id);
    }
}
