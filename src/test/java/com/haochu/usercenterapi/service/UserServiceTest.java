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
}