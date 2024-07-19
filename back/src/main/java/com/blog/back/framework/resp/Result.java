package com.blog.back.framework.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据返回类
 */
@Data
@Accessors(chain = true)
public class Result {

    private Object data;

    private ResultState state;

    private String msg;

    public static Result success(){
        return Result.success(null,ResultState.SUCCESS,"成功！");
    }

    public static Result success(Object data){
        return Result.success(data,ResultState.SUCCESS,"成功！");
    }

    public static Result success(Object data,ResultState state){
        return Result.success(data,state,"成功！");
    }

    public static Result success(Object data,String msg){
        return Result.success(data,ResultState.SUCCESS,msg);
    }

    public static Result success(Object data,ResultState state,String msg){
        return new Result()
                .setData(data)
                .setMsg(msg)
                .setState(state);
    }

    public static Result fail(){
        return Result.fail(null,ResultState.FAIL,"失败！");
    }

    public static Result fail(Object data){
        return Result.fail(data,ResultState.FAIL,"失败！");
    }

    public static Result fail(Object data,ResultState state){
        return Result.fail(data,state,"失败！");
    }

    public static Result fail(Object data,String msg){
        return Result.fail(data,ResultState.FAIL,msg);
    }

    public static Result fail(Object data,ResultState state,String msg){
        return new Result()
                .setData(data)
                .setMsg(msg)
                .setState(state);
    }
}
