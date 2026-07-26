package com.kaede.erp.common.exception;


import com.kaede.erp.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(
            BusinessException e
    ){

        return Result.error(
                e.getCode(),
                e.getMessage()
        );

    }



    @ExceptionHandler(Exception.class)
    public Result<String> handleException(
            Exception e
    ){

        return Result.error(
                50000,
                e.getMessage()
        );

    }

}