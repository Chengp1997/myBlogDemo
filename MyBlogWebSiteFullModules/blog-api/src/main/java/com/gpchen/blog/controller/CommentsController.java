package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.CommentParams;
import com.gpchen.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{id}")
    @LogAnnotation(module="comments", operation="all comments")
//    @Cache(expire = 5 * 60 * 1000,name = "list_all_comments")
    public Result listArticleComments(@PathVariable("id") Long articleId){
        return commentService.listArticleComments(articleId);
    }

    @PostMapping("/create/change")
    @LogAnnotation(module="comments", operation="give comments")
    public Result giveComments(@RequestBody CommentParams commentParams){
        return commentService.giveComments(commentParams);
    }
}
