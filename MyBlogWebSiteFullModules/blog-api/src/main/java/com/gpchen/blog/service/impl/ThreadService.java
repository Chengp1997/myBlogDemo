package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gpchen.blog.dao.ArticleMapper;
import com.gpchen.blog.model.entity.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * All the thread service the blog used. putting some non-important service in the thread can help
 * improve the efficiency of the program
 */
@Component
public class ThreadService {

    /**
     * use the thread service to update the view count.
     * main process-- open the article page, and it needs to retrieve the data from db
     * however, if it needs to update viewCounts at the same time,
     * which means it needs to retrieve the view counts form db
     * and update the viewCounts and save it to db in case there are multiple users
     * if there is only one process, this will cost so much time, but we can take advantage of multiple thread
     * main process if to open the article page
     * the multiple thread service is on the charge of updating counts
     * @param articleMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        int viewCount = article.getViewCounts();
        Article updateArticle = new Article();//为了最小限度的修改，增加一个新的对象，最后只把这个对象的情况更新进去
        updateArticle.setViewCounts(viewCount+1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        //it is used similar as CAS, to prevent the case that other thread change the value first
        queryWrapper.eq(Article::getViewCounts,viewCount);
        //update article set viewCount=100 where viewCount=99 && id=1
        articleMapper.update(updateArticle,queryWrapper);
    }

}
