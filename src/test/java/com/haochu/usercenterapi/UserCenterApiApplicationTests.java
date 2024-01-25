package com.haochu.usercenterapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterApiApplicationTests {
    @Test
    void testDigest() {
        String result = DigestUtils.md5DigestAsHex(("haochu" + "abc").getBytes());
        Assertions.assertNotEquals("",result);
    }

}
