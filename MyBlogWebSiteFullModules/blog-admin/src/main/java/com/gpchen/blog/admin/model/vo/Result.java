package com.gpchen.blog.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这个是返回信息的格式，如果成功返回/返回失败，希望获得的信息格式。可以理解成是封装的一层信息。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    //是否成功
    private boolean success;
    //返回码
    private int code;
    //传回来的信息
    private String message;
    //数据
    private Object data;

    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    public static Result fail(int code, String msg){
        return new Result(false,code,msg,null);
    }
}
