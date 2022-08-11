package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.params.ArticleParams;
import com.gpchen.blog.service.ArticleService;
import com.gpchen.blog.model.vo.params.ArticlePageParams;
import com.gpchen.blog.model.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @LogAnnotation(module="articles", operation="All Articles")
//    @Cache(expire = 5 * 60 * 1000,name = "all_articles")
    public Result listArticles(@RequestBody ArticlePageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    @PostMapping("/hot")
    @LogAnnotation(module="articles", operation="Hottest Articles")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result getHottestArticle(){
        int limit = 5;
        return articleService.getHottestArticles(limit);
    }

    @PostMapping("/new")
    @LogAnnotation(module="articles", operation="Newest Articles")
//    @Cache(expire = 5 * 60 * 1000,name = "new_article")
    public Result getNewestArticles(){
        int limit = 5;
        return articleService.getNewestArticles(limit);
    }

    @PostMapping("/listArchives")
    @LogAnnotation(module="articles", operation="list Archives")
//    @Cache(expire = 5 * 60 * 1000,name = "archives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("/view/{id}")
    @LogAnnotation(module="articles", operation="Article Details")
    public Result articleDetails(@PathVariable("id") long id){
        return articleService.findArticleById(id);
    }

    @PostMapping("/publish")
    @LogAnnotation(module="articles", operation="publish Articles")
    public Result publishArticle(@RequestBody ArticleParams articleParams){
        return articleService.publishArticle(articleParams);
    }

    @PostMapping("{id}")
    @LogAnnotation(module="articles", operation="Article Details")
    public Result articleById(@PathVariable("id") long id){
        return articleService.findArticleById(id);
    }

    @PostMapping("/search")
    public Result searchArticles(@RequestBody ArticleParams articleParams){
        String search =articleParams.getSearch();
        return articleService.searchArticles(search);
    }

}
