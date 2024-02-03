package com.haochu.usercenterapi.common;

/**
 * 返回工具类
 *
 * @author haochu
 */
public class ResultUtils<T> {
    /**
     * 成功
     *
     * @param data 成功数据
     * @param <T> 泛型
     * @return 请求信息
     *
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"success");
    }

    /**
     * 失败
     *
     * @param errorCode 枚举错误信息
     * @return 请求信息
     */
    public static <T> BaseResponse <T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

    /**
     * 自定义 code,message,description
     *
     * @param code 错误代码
     * @param message 错误信息
     * @param description 错误详情
     * @return 请求信息
     */
    public static <T> BaseResponse <T> error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }
}
