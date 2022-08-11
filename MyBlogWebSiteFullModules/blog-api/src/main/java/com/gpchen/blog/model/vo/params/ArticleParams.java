package com.gpchen.blog.model.vo.params;

import com.gpchen.blog.model.vo.CategoryVo;
import com.gpchen.blog.model.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParams {
    private Long id;

    private ArticleBodyParams body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    private String search;
}
