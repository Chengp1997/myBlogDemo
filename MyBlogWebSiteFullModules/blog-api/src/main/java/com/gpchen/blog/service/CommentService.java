package com.gpchen.blog.service;

import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.CommentParams;

public interface CommentService {
    Result listArticleComments(Long articleId);

    Result giveComments(CommentParams commentParams);
}
