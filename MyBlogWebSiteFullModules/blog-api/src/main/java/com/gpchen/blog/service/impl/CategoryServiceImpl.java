package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gpchen.blog.dao.CategoryMapper;
import com.gpchen.blog.model.entity.Article;
import com.gpchen.blog.model.entity.Category;
import com.gpchen.blog.model.vo.CategoryVo;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.service.ArticleService;
import com.gpchen.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result listAllCategories() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>());
        return Result.success(copyList(categories));
    }

    @Override
    public Result getCategoryDetails() {
        return Result.success(copyList(categoryMapper.selectList(new LambdaQueryWrapper<Category>())));
    }

    @Override
    public Result getCategoryDetailsById(String categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category:categories){
            CategoryVo categoryVo = copy(category);
            categoryVoList.add(categoryVo);
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
