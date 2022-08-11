package com.gpchen.blog.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

//数据库直接取出来的原数据很明显不能直接传达给页面，需要进行一个处理再传递给页面，因此再这中间再加一层view boject用来进行二次处理
@Data
public class ArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Integer commentCounts;

    private String summary;

    private String title;

    private Integer viewCounts;

    private Integer weight;

    //原数据是long，这里改成String，只需要获得对应日期就可以
    private String createDate;

    //原数据中存储的是author id，这里很明显我只想知道author是谁，不想知道id是什么
//    private String author;
    private UserVo author;

    //原数据存储的是ArticleBodyId, 最终希望获得的肯定是对应的对象。
    private ArticleBodyVo body;

    //原数据并没有tag，tag不是Article的默认属性，这里通过这个方法把Article和tag通过article_tag这张表联合起来
    private List<TagVo> tags;

    //原数据只有对应分类的id，但是这里要显示的最终的所有分类
//    private List<CategoryVo> categories;
    private CategoryVo category;
}
