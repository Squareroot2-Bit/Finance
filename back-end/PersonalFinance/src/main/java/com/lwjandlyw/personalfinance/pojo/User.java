package com.lwjandlyw.personalfinance.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName User
 * @Description
 * @date 2024/6/10 22:47
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int user_id;
    String username;
    String password;
    String nickname;
}
