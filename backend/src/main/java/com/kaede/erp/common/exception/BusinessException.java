package com.kaede.erp.common.exception;


import com.kaede.erp.common.constant.ResultCode;
import lombok.Getter;


@Getter
public class BusinessException extends RuntimeException {


    private final Integer code;


    public BusinessException(ResultCode resultCode){

        super(resultCode.getMessage());

        this.code = resultCode.getCode();

    }


    public BusinessException(Integer code, String message){

        super(message);

        this.code = code;

    }
}