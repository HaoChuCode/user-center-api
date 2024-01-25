package com.haochu.usercenterapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haochu.usercenterapi.service.UserService;
import com.haochu.usercenterapi.model.User;
import com.haochu.usercenterapi.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author haochu
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
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
        String validPattern = "^[A-Za-z0-9_-]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(!matcher.find()){
            return -4;
        }
        //密码和校验码是否相同
        if(!userPassword.equals(checkPassword)){
            return -5;
        }
        //账户重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -6;
        }

        //加密密码
        final String SALT = "haochu";
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -7;
        }
        return user.getId();
    }
}




