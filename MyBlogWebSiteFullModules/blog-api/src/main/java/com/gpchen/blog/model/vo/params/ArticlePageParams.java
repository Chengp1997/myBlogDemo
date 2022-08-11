package com.gpchen.blog.model.vo.params;

import lombok.Data;

@Data
public class ArticlePageParams {
    //当前页
    private int page =1;
    //每页显示的数量
    private int pageSize = 10;

    private Long categoryId;

    private Long tagId;

    //下面两个新的参数，用来对归档的时候做处理。
    private String year;

    private String month;
    //把6变成06
    public String getMonth(){
        if(this.month!=null && this.month.length()==1){
            return "0"+this.month;
        }
        return month;
    }
}
