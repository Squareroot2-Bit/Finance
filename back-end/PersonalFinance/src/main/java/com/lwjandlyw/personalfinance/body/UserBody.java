package com.lwjandlyw.personalfinance.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserBody
 * @Description
 * @date 2024/6/10 23:46
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBody {
    String username;
    String password;
}
