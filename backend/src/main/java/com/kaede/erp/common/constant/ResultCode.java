package com.kaede.erp.common.constant;


import lombok.Getter;


@Getter
public enum ResultCode {


    SUCCESS(200, "success"),

    PARAM_ERROR(40001, "参数错误"),

    USER_NOT_FOUND(40002, "用户不存在"),

    SYSTEM_ERROR(50000, "系统异常");


    private final Integer code;

    private final String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}