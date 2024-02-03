package com.haochu.usercenterapi.controller;

import com.haochu.usercenterapi.common.BaseResponse;
import com.haochu.usercenterapi.common.ErrorCode;
import com.haochu.usercenterapi.common.ResultUtils;
import com.haochu.usercenterapi.exception.BusinessException;
import com.haochu.usercenterapi.model.User;
import com.haochu.usercenterapi.model.request.UserLoginRequest;
import com.haochu.usercenterapi.model.request.UserRegisterRequest;
import com.haochu.usercenterapi.service.UserService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.haochu.usercenterapi.contant.UserConstant.ADMIN_ROLE;
import static com.haochu.usercenterapi.contant.UserConstant.USER_LOGIN_STATUS;

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
    public BaseResponse<Long> userRegister(
            @RequestBody UserRegisterRequest userRegisterRequest
    ) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String registerCode = userRegisterRequest.getRegisterCode();
        if (!StringUtils.isNoneBlank(userAccount, userPassword,registerCode, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, registerCode, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userRegister(
            @RequestBody UserLoginRequest userLoginRequest,
            HttpServletRequest request
    ) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (!StringUtils.isNoneBlank(userAccount, userPassword)) {
            return null;
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(
            HttpServletRequest request
    ) {
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("list")
    public BaseResponse<List<User>> listUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
        //鉴权 是否位管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<User> result = userService.listUsers(username, request);
        return ResultUtils.success(result);
    }

    @PostMapping("delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //鉴权 是否位管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = userService.deleteUser(id, request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        User byIdUser = userService.getById(userId);
        User result = userService.getSafetyUser(byIdUser);
        return ResultUtils.success(result);
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
