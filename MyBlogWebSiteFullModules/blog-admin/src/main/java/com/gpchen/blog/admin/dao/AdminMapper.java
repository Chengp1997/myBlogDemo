package com.gpchen.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpchen.blog.admin.model.entity.Admin;
import com.gpchen.blog.admin.model.entity.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("Select * from permission where id in (select permission_id from admin_permission where admin_id=#{adminId}) ")
    List<Permission> findPermissionByAdminId(Long adminId);
}
