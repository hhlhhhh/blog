package com.blog.back.framework.exception.handler;

import com.blog.back.framework.resp.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = "参数错误！";
        if(fieldError!=null){
            message = fieldError.getDefaultMessage();
        }
        return Result.fail(null,message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result authenticationExceptionExceptionHandler(AuthenticationException exception){
        exception.printStackTrace();
        return Result.fail(null,"认证失败！");
    }
//
//
//
//    @ExceptionHandler(RuntimeException.class)
//    public Result runtimeExceptionHandler(RuntimeException exception){
//        exception.printStackTrace();
//        return Result.fail(null,"系统异常");
//    }
//
//    @ExceptionHandler(Exception.class)
//    public Result exceptionHandler(Exception exception){
//        exception.printStackTrace();
//        return Result.fail(null,"系统异常");
//    }

}
