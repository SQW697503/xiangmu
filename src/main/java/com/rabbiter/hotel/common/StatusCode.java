package com.rabbiter.hotel.common;

public enum StatusCode {
    // 成功
    COMMON_SUCCESS(ConstantCode.SUCCESS, "成功。"),
    // 失败
    COMMON_FAIL(ConstantCode.FAIL, "失败")
    ;

    // 状态码
    private Integer code;
    // 消息
    private String message;

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取消息
     *
     * @return 消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 默认构造方法
     */
    StatusCode() {
    }

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 消息
     */
    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
