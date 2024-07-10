package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.UserBody;
import com.lwjandlyw.personalfinance.pojo.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        //事务会自动回滚，数据不会插入到数据库中
    void registration() {
        //测试注册新用户
        UserBody userBody = new UserBody("TangShiping", "000000");
        int registration = service.registration(userBody);
        assert registration == 1;
        List<User> users = service.userMapper.selectUserByUsername("TangShiping");
        assert users.size() == 1;
        //测试重复注册用户
        int registrationAgain = service.registration(userBody);
        assert registrationAgain == -1;
    }


    @Test
    @Transactional
    void login() {
        //测试登录成功
        UserBody userBody0 = new UserBody("LiuWenjing", "000000");
        int login0 = service.login(userBody0);
        assert login0 == 1;
        //测试用户不存在
        UserBody userBody1 = new UserBody("TangShiping", "000000");
        int login1 = service.login(userBody1);
        assert login1 == -1;
        //测试密码错误
        UserBody userBody2 = new UserBody("LiuWenjing", "114514");
        int login2 = service.login(userBody2);
        assert login2 == -2;
    }
}