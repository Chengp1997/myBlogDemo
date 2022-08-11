package com.gpchen.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gpchen.blog.model.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 因为mybatis不支持多表查询，因此需要配置xml文件，再xml文件中手写sql进行多表查询
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> getHottestTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
