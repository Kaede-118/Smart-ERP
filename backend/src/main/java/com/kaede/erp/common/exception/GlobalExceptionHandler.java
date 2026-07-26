package com.kaede.erp.common.exception;


import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<String> handleAccessDeniedException(
            AccessDeniedException e
    ){

        return Result.error(
                ResultCode.FORBIDDEN
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