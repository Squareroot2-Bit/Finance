package com.lwjandlyw.personalfinance.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName Response
 * @Description
 * @date 2024/5/20 23:36
 * @Author Squareroot_2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {
    private Integer code;
    private String message;
    private Object data;
}
