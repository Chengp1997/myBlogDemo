package com.gpchen.blog.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Comment {
    //防止前端 精度损失 把id转为string
// 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输，就不会有问题了
    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;
}
