package com.haochu.usercenterapi.service;
import java.util.Date;

import com.haochu.usercenterapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户服务测试
 *
 * @author haochu
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("testHaoChu");
        user.setUserAccount("admin");
        user.setAvatar("https://images.zsxq.com/FrnKUO745Uq9VH_FCv0TkKiSBmiO?e=1709222399&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:pj_h72xye15JISB8zameJoZpBII=");
        user.setGender(0);
        user.setUserPassword("admin");
        user.setPhone("123");
        user.setEmail("456");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        //非空校验
        String userAccount = "";
        String userPassword = "123456";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //账户不小于4位
        userAccount = "hao";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-2,result);
        //账户不能重复
        userAccount = "admin";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-6,result);
        //账户不包含特殊字符
        userAccount = "hao@chu";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-4,result);
        //密码不小于6位
        userAccount = "haochu";
        userPassword = "123";
        checkPassword = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-3,result);
        //校验密码和密码相同
        userAccount = "haochu";
        userPassword = "123456";
        checkPassword = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-5,result);
        //成功校验
        userAccount = "haochu";
        userPassword = "1234567";
        checkPassword = "1234567";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);

    }
}