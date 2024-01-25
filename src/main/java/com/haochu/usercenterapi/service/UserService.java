package com.haochu.usercenterapi.service;

import com.haochu.usercenterapi.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务
 *
* @author haochu
*/
public interface UserService extends IService<User> {
    /**
     * 用户注释
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
