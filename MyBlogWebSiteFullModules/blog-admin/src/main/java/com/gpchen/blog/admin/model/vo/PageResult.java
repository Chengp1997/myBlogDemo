package com.gpchen.blog.admin.model.vo;

import lombok.Data;

import java.util.List;

//最后要返回的，应该是带分页的数据，
@Data
public class PageResult<T> {
    private List<T> list;
    Long total;
}
