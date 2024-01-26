package com.haochu.usercenterapi.controller;

import com.haochu.usercenterapi.model.User;
import com.haochu.usercenterapi.model.request.UserLoginRequest;
import com.haochu.usercenterapi.model.request.UserRegisterRequest;
import com.haochu.usercenterapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.haochu.usercenterapi.contant.UserContant.ADMIN_ROLE;
import static com.haochu.usercenterapi.contant.UserContant.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author haochu
 */
@RestController
@RequestMapping("users")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(
            @RequestBody UserRegisterRequest userRegisterRequest
    ) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (!StringUtils.isNoneBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userRegister(
            @RequestBody UserLoginRequest userLoginRequest,
            HttpServletRequest request
    ) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (!StringUtils.isNoneBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("list")
    public List<User> listUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
        //鉴权 是否位管理员
        if (!isAdmin(request)) {
            return null;
        }
        return userService.listUsers(username, request);
    }

    @PostMapping("delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        //鉴权 是否位管理员
        if (!isAdmin(request)) {
            return false;
        }
        return userService.deleteUser(id, request);
    }

    /**
     * 是否为管理员
     *
     * @param request http返回
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
