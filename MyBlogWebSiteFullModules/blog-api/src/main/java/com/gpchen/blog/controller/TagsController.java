package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.service.TagService;
import com.gpchen.blog.model.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    @GetMapping
    @LogAnnotation(module="tags", operation="All tags")
//    @Cache(expire = 5 * 60 * 1000,name = "list_all_tags")
    public Result listAllTags(){
        return tagService.listAllTags();
    }

    @GetMapping("/hot")
    @LogAnnotation(module="tags", operation="Hot tags")
//    @Cache(expire = 5 * 60 * 1000,name = "hot_tags")
    public Result getHottestTag(){
        int limit = 5;
        return tagService.getHottestTag(limit);
    }

    @GetMapping("/detail")
    @LogAnnotation(module="tags", operation="Tag Details")
//    @Cache(expire = 5 * 60 * 1000,name = "get_tag_detail")
    public Result getTagsDetail(){
        return tagService.getTagsDetail();
    }

    @GetMapping("/detail/{id}")
    @LogAnnotation(module="tags", operation="Get Articles by Tag")
//    @Cache(expire = 5 * 60 * 1000,name = "get_articles_by_tag")
    public Result getArticlesByTags(@PathVariable("id") String tagId){
        return tagService.getTagDetailsById(tagId);
    }
}
