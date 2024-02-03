package com.haochu.usercenterapi.common;

/**
 * 错误码
 *
 * @author haochu
 */
public enum ErrorCode {
    SUCCESS(0,"success",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    PARAMS_NUll_ERROR(40001,"请求数据为空",""),
    NO_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常",""),
    INSERT_DATA_ERROR(50001,"插入数据失败",""),
    DELETE_DATA_ERROR(50002,"删除数据失败","");

    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 详情
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
