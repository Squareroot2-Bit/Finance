package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.UserBody;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName UserServiceTest
 * @Description
 * @date 2024/7/10 21:33
 * @Author Squareroot_2
 */
@SpringBootTest
@MapperScan("com.lwjandlyw.personalfinance.mapper")
class UserServiceTest {

    @Autowired
    UserService service;
    @Test
    @Transactional
    void registrationTest() {
        UserBody userBody = new UserBody("1234567", "123456789");
        int registration = service.registration(userBody);
        assert registration == 1;
    }
}