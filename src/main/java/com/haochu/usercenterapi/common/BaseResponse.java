package com.haochu.usercenterapi.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 * @author haochu
 */
@Data
public class BaseResponse <T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2735827475888216332L;
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code, T data,String message) {
        this(code,data,message,null);
    }

}
