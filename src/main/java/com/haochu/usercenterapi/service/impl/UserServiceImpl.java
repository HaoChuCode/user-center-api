package com.haochu.usercenterapi.service.impl;

import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haochu.usercenterapi.common.ErrorCode;
import com.haochu.usercenterapi.exception.BusinessException;
import com.haochu.usercenterapi.service.UserService;
import com.haochu.usercenterapi.model.User;
import com.haochu.usercenterapi.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haochu.usercenterapi.contant.UserConstant.DEFAULT_AVATAR;
import static com.haochu.usercenterapi.contant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户服务实现类
 *
 * @author haochu
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值
     */
    private static final String SALT = "haochu";
    private static final String validPattern = "^[A-Za-z0-9_-]+$";


    @Override
    public long userRegister(String userAccount, String userPassword, String registerCode, String checkPassword) {
        //校验
        if (!StringUtils.isNoneBlank(userAccount, registerCode ,userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"其中有参数为空");
        }
        //账户和密码过短
        userParamsLess(userAccount,userPassword);

        //账户不能包含特殊字符
        userAccountError(userAccount);

        //密码和校验码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //账户重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户已存在");
        }
        //注册编号重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("register_code", registerCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该注册码已使用");
        }

        //加密密码
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setRegisterCode(registerCode);
        user.setUserPassword(md5Password);
        user.setAvatar(DEFAULT_AVATAR);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.INSERT_DATA_ERROR,"插入用户数据失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (!StringUtils.isNoneBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"其中有参数为空");
        }
        //账户和密码过短
        userParamsLess(userAccount,userPassword);
        //账户不能包含特殊字符
        userAccountError(userAccount);

        //加密密码
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", md5Password);
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }

        //用户脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);

        return safetyUser;
    }

    @Override
    public List<User> listUsers(String username, HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        List<User> safetyUsers = new ArrayList<>();
        for(User user:users){
            User safetyUser = getSafetyUser(user);
            safetyUsers.add(safetyUser);
        }
        return safetyUsers;
    }

    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.DELETE_DATA_ERROR,"该用户ID不存在");
        }
        return this.removeById(id);
    }

    public User getSafetyUser(User user) {
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_NUll_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setRegisterCode(user.getRegisterCode());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatar(user.getAvatar());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(user.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request http返回
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }

    /**
     * 登录注册参数过短
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     */
    private void userParamsLess(String userAccount,String userPassword){
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度不能小于4");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码长度不能小于6");
        }
    }

    /**
     * 账户包含特殊字符
     * @param userAccount 账户名
     */
    private void userAccountError(String userAccount){
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能包含特殊字符");
        }
    }


}




