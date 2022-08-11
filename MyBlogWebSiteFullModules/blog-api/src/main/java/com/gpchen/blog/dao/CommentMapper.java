package com.gpchen.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpchen.blog.model.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
