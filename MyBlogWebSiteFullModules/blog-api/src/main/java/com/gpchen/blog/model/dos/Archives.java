package com.gpchen.blog.model.dos;

import lombok.Data;

//do ： data object，do not need to be persist。
@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;
}
