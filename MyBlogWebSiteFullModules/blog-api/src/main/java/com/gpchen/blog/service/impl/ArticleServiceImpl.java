package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gpchen.blog.dao.ArticleBodyMapper;
import com.gpchen.blog.dao.ArticleMapper;
import com.gpchen.blog.dao.ArticleTagMapper;
import com.gpchen.blog.model.dos.Archives;
import com.gpchen.blog.model.entity.*;
import com.gpchen.blog.model.vo.*;
import com.gpchen.blog.model.vo.params.ArticleParams;
import com.gpchen.blog.service.ArticleService;
import com.gpchen.blog.service.CategoryService;
import com.gpchen.blog.service.SysUserService;
import com.gpchen.blog.service.TagService;
import com.gpchen.blog.model.vo.params.ArticlePageParams;
import com.gpchen.blog.util.UserThreadLocal;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;//原本想再建立一个，后来想想其实没必要，因为毕竟body就是文章的一部分
    @Autowired
    private ArticleTagMapper articleTagMapper;
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ThreadService threadService;

    @Override
    public Result listArticle(ArticlePageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper
                .listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

    @Override
    public Result getHottestArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);//排序条件
        queryWrapper.select(Article::getId,Article::getTitle);//选择栏目
        queryWrapper.last("limit "+limit);//限定条数

        //获得数据
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));//不需要其他任何数据了，因此都是false
    }

    @Override
    public Result getNewestArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);

        List<Article> articles=articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    //list archives based on year, month
    @Override
    public Result listArchives() {
        List<Archives> archives = articleMapper.listArchives();
        return Result.success(archives);
    }

    @Override
    public Result findArticleById(long id) {
        Article article = articleMapper.selectById(id);
        threadService.updateArticleViewCount(articleMapper,article);
        ArticleVo articleVo = copy(article,true,true,true,true);
        return Result.success(articleVo);
    }

    @Override
    public Result publishArticle(ArticleParams articleParams) {
        boolean isEdit = false;
        Article article = new Article();
        //if id is not null , isEdit = true
        if(articleParams.getId()!=null){
            //set all the things need to be showed on the page
            article.setId(articleParams.getId());
            article.setTitle(articleParams.getTitle());
            article.setSummary(articleParams.getSummary());
            article.setCategoryId(articleParams.getCategory().getId());
            articleMapper.updateById(article);
            isEdit=true;
        }else{//if is not edit, write new
            article.setAuthorId(UserThreadLocal.get().getId());
            article.setCategoryId(articleParams.getCategory().getId());
            article.setCreateDate(System.currentTimeMillis());
            article.setCommentCounts(0);
            article.setSummary(articleParams.getSummary());
            article.setTitle(articleParams.getTitle());
            article.setViewCounts(0);
            article.setWeight(Article.Article_Common);
            article.setBodyId(-1L);//先设置默认值，因为后面body需要article id，必须先保存一次
            articleMapper.insert(article);
        }
        System.out.println("0000000+"+articleParams.getCategory().getId());
        //还有bodyId没有设置
        //article_body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());//必须得保存了才能生成id，所以前面必须保存一次
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        //article_tag
        List<TagVo> tagVos = articleParams.getTags();
        for(TagVo tagVo : tagVos){
            Long articleId = article.getId();
            if(isEdit){//if is edit, need to show on the page
                LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ArticleTag::getArticleId,articleId);
                articleTagMapper.delete(queryWrapper);//delete first and insert again
            }
             ArticleTag articleTag = new ArticleTag();
             articleTag.setArticleId(article.getId());
             articleTag.setTagId(tagVo.getId());
             this.articleTagMapper.insert(articleTag);
        }
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
//        if (isEdit){
//            //send message to rocketmq to update the cache, tell mq, message is articleID
//            ArticleMessage articleMessage = new ArticleMessage();
//            articleMessage.setArticleId(article.getId());
////            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
//        }
        return Result.success(articleVo);//注意！！传回去的一定是一个articleVO，是对象！！不是值！
    }

    @Override
    public Result searchArticles(String search) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.like(Article::getTitle,search);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    private List<ArticleVo> copyList(List<Article> articles,boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article article:articles){
            articleVoList.add(copy(article,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> articles,boolean isTag, boolean isAuthor,boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article article:articles){
            articleVoList.add(copy(article,isTag,isAuthor,isBody,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> articles,boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article article:articles){
            articleVoList.add(copy(article,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    /**
     *
     * @param article
     * @param isTag
     * @param isAuthor
     * @return
     */
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody, boolean isCategory){
        ArticleVo articleVo=new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);//把所有相同属性的东西全部复制，但是不相同的没法复制，因此下面需要转格式
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有都需要tags和author，因此要根据情况进行传值
        if(isTag){
            List<TagVo> tagVos = tagService.findTagsByArticleId(article.getId());
            //我们要找到新的需要传的tagsVo
            articleVo.setTags(tagVos);
        }
        if(isAuthor){
            UserVo userVo = sysUserService.findUserById(article.getAuthorId());
            articleVo.setAuthor(userVo);
        }
        if(isBody){
            ArticleBodyVo articleBodyVo =findArticleBodyById(article.getBodyId());
            articleVo.setBody(articleBodyVo);
        }
        if(isCategory){
            CategoryVo categoryVo = categoryService.findCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(long id){
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
