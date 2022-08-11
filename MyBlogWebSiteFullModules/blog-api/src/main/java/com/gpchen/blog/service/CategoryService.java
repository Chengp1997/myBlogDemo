package com.gpchen.blog.service;

import com.gpchen.blog.model.vo.CategoryVo;
import com.gpchen.blog.model.vo.Result;
import org.springframework.stereotype.Service;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);
    Result listAllCategories();

    Result getCategoryDetails();

    Result getCategoryDetailsById(String categoryId);
}
