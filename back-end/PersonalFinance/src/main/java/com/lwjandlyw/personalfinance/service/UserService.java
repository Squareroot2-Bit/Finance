package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.UserBody;
import com.lwjandlyw.personalfinance.mapper.UserMapper;
import com.lwjandlyw.personalfinance.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserService
 * @Description
 * @date 2024/6/10 23:40
 * @Author Squareroot_2
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public int registration(UserBody userBody) {
        if (userMapper.selectUserByUsername(userBody.getUsername()).isEmpty()) {
            User user = new User();
            user.setUser_id(0);
            user.setUsername(userBody.getUsername());
            user.setPassword(userBody.getPassword());
            user.setNickname(userBody.getUsername());
            return userMapper.insert(user);
        } else return -3;
    }

    public int login(UserBody userBody) {
        List<User> users = userMapper.selectUserByUsername(userBody.getUsername());
        if (users.isEmpty())
            return -1;
        User user = users.get(0);
        if (user.getPassword().equals(userBody.getPassword()))
            return user.getUser_id();
        else return -2;
    }
}
