package com.haochu.usercenterapi.exception;

import com.haochu.usercenterapi.common.ErrorCode;
import lombok.Data;

/**
 * 自定义异常类
 *
 * @author haochu
 */
@Data
public class BusinessException extends RuntimeException{
    private final int code;
    private final String message;
    private final String description;

    public BusinessException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode, String description) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = description;
    }
}
