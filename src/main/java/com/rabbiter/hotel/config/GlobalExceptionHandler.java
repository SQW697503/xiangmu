package com.rabbiter.hotel.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice // 控制器通知，用于处理全局异常
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     *
     * @param e 异常对象
     * @return 响应实体
     */
    @ExceptionHandler(value = Exception.class) // 表示该方法用于处理 Exception 类型的异常
    @ResponseBody // 响应体为字符串形式
    public ResponseEntity<String> handleException(Exception e) {
        // 自定义异常处理逻辑
        String message = e.getMessage(); // 获取异常消息
        e.printStackTrace(); // 打印异常栈轨迹信息
        // 根据异常消息进行分类处理
        if (message.contains("(using password: YES)")) { // 如果数据库连接失败
            if (!message.contains("'root'@'")) { // 不是本地 MySQL 数据库连接错误
                message = "PU Request failed with status code 500";
            } else if (message.contains("'root'@'localhost'")) { // 是本地 MySQL 数据库连接错误
                message = "P Request failed with status code 500";
            }
        } else if (message.contains("Table") && message.contains("doesn't exist")) { // 如果数据库表不存在
            message = "T Request failed with status code 500";
        } else if (message.contains("Unknown database")) { // 如果数据库不存在
            message = "U Request failed with status code 500";
        } else if (message.contains("edis")) { // 如果 Redis 配置错误
            message = "R Request failed with status code 500";
        } else if (message.contains("Failed to obtain JDBC Connection")) { // 如果获取数据库连接失败
            message = "C Request failed with status code 500";
        } else if (message.contains("SQLSyntaxErrorException")) { // 如果 SQL 语法错误
            message = "S Request failed with status code 500";
        }
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR); // 返回响应实体，设置响应状态码为 500（INTERNAL_SERVER_ERROR）
    }
}
