package com.haochu.usercenterapi.service.impl;

import java.util.ArrayList;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

import static com.haochu.usercenterapi.contant.UserContant.USER_LOGIN_STATUS;

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
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验
        if (!StringUtils.isNoneBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -2;
        }
        if (userPassword.length() < 6) {
            return -3;
        }

        //账户不能包含特殊字符
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            return -4;
        }
        //密码和校验码是否相同
        if (!userPassword.equals(checkPassword)) {
            return -5;
        }
        //账户重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -6;
        }

        //加密密码
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -7;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (!StringUtils.isNoneBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 6) {
            return null;
        }

        //账户不能包含特殊字符
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            return null;
        }

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
            return null;
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
            return false;
        }
        return this.removeById(id);
    }


    /**
     * 用户脱敏
     *
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    private User getSafetyUser(User user) {
        User safetyUser = new User();
        safetyUser.setId(user.getId());
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


}




