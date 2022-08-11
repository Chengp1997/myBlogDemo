package com.gpchen.blog.service;

import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.ArticlePageParams;
import com.gpchen.blog.model.vo.params.ArticleParams;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArticleService {
    /**
     * 首页的分页显示文章列表
     * @param articlePageParams
     * @return
     */
    Result listArticle(ArticlePageParams articlePageParams);

    Result getHottestArticles(int limit);

    Result getNewestArticles(int limit);

    Result listArchives();

    Result findArticleById(long id);

    Result publishArticle(ArticleParams articleParams);

    Result searchArticles(String search);
}
