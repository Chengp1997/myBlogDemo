package com.gpchen.blog.handler;

import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//AOP, exception occurred for on controller, This advice would trigger to handle and print out the trace
@ControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody  //The exception will reply as json
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
    }
}
