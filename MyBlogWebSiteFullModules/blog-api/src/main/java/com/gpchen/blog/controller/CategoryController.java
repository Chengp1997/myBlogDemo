package com.gpchen.blog.controller;

import com.gpchen.blog.common.cache.Cache;
import com.gpchen.blog.common.log.LogAnnotation;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @LogAnnotation(module="category", operation="All categories")
//    @Cache(expire = 5 * 60 * 1000,name = "list_all_categories")
    public Result listAllCategories(){
        return categoryService.listAllCategories();
    }

    @GetMapping("/detail")
    @LogAnnotation(module="category", operation="Category details")
//    @Cache(expire = 5 * 60 * 1000,name = "category_detail")
    public Result getCategoryDetails(){
        return categoryService.getCategoryDetails();
    }

    @GetMapping("/detail/{id}")
    @LogAnnotation(module="category", operation="All articles in the category")
//    @Cache(expire = 5 * 60 * 1000,name = "get_all_articles_in_category")
    public Result getArticlesByCategory(@PathVariable("id") String categoryId){
        return categoryService.getCategoryDetailsById(categoryId);
    }
}
