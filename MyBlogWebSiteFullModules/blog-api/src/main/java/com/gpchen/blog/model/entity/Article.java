package com.gpchen.blog.model.entity;

import lombok.Data;

@Data
public class Article {

    public static final int Article_TOP = 1;
    public static final int Article_Common = 0;

    private Long id;

    private Integer commentCounts;

    private String summary;

    private String title;

    private Integer viewCounts;

    // is top or not default: not on the top
    private Integer weight;

    //first created time
    private Long createDate;
    //author id
    private Long authorId;
    //article_content Id
    private Long bodyId;
    // category Id
    private Long categoryId;


}
