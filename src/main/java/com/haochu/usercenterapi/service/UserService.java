package com.haochu.usercenterapi.service;

import com.haochu.usercenterapi.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author haochu
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 条件查询用户信息
     *
     * @param username 用户昵称
     * @param request http返回
     *
     * @return 用户信息
     */
    List<User> listUsers(String username, HttpServletRequest request);

    /**
     * 删除用户数据
     *
     * @param id 根据ID删除
     * @param request http返回
     *
     * @return 是否删除成功
     */
    boolean deleteUser(long id, HttpServletRequest request);

}
