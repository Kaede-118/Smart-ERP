package com.kaede.erp.common.constant;

import lombok.Getter;

@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 请求参数错误
     */
    PARAM_ERROR(40000, "参数错误"),

    /**
     * 未登录
     */
    UNAUTHORIZED(40100, "未登录"),

    /**
     * 无权限
     */
    FORBIDDEN(40300, "无权限"),

    /**
     * 资源不存在
     */
    NOT_FOUND(40400, "资源不存在"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(40401, "用户不存在"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}