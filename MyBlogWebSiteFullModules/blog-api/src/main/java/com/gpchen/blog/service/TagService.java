package com.gpchen.blog.service;

import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result getHottestTag(int limit);

    Result listAllTags();

    Result getTagsDetail();

    Result getTagDetailsById(String tagId);
}
