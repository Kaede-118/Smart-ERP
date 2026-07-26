package com.kaede.erp.common.exception;


import com.kaede.erp.common.constant.ResultCode;
import com.kaede.erp.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(
            BusinessException e
    ){

        System.out.println("[BizException] code=" + e.getCode() + " msg=" + e.getMessage());

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

        System.out.println("[Security] AccessDenied: " + e.getMessage());

        return Result.error(
                ResultCode.FORBIDDEN
        );

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(
            MethodArgumentNotValidException e
    ){

        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数错误");

        System.out.println("[Validation] " + msg);

        return Result.error(
                ResultCode.PARAM_ERROR.getCode(),
                msg
        );

    }


    @ExceptionHandler(Exception.class)
    public Result<String> handleException(
            Exception e
    ){

        System.err.println("[SystemError] " + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace();

        return Result.error(
                ResultCode.SYSTEM_ERROR.getCode(),
                e.getClass().getSimpleName() + ": " + e.getMessage()
        );

    }

}
