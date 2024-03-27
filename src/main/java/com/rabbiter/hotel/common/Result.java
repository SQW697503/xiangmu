package com.rabbiter.hotel.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private Object data; //返回的数据

    //请求参数缺失:404001
    public static Result PARAM_EMPTY = new Result(404001, "请求参数缺失", "请求参数缺失");
    //查询内容缺失:404002
    public static Result QUERY_EMPTY = new Result(404002, "查询内容不存在", "查询内容不存在");
    //信息已存在:500001
    public static Result QUERY_EXISTS = new Result(500001, "信息已存在！", "信息已存在！");

    public static Result CONTRACT_ERROR = new Result(500002, "智能合约请求存在问题","错误！！");

    public static Result CONTRACT_ERROR1 = new Result(600002, "用户名或密码错误","用户名或密码错误");

    public static Result TOKEN_EMPTY = new Result(500003, "Token已失效","Token已失效");

    public static Result ADDRESS_INVALID = new Result(500004, "操作地址不合法","操作地址不合法");

    public static Result NOT_LOGIN = new Result(0, "NOT_LOGIN","未登录");


    //增删改 成功响应
    public static Result success(){
        return new Result(1,"success",null);
    }
    //查询 成功响应
    public static Result success(Object data){
        return new Result(1,"success",data);
    }
    //失败响应
    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
