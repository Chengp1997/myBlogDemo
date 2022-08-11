package com.gpchen.blog.admin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {
    @TableId(type = IdType.AUTO)//后台管理没必要分布式id了，东西不多
    private Long id;
    private String name;
    private String path;
    private String description;
}
