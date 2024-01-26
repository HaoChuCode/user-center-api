package com.haochu.usercenterapi.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author haochu
 */
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 4369670712451570278L;
    private String userAccount;
    private String userPassword;
}
