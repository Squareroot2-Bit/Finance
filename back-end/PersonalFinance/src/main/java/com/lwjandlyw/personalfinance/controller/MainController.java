package com.lwjandlyw.personalfinance.controller;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.body.UserBody;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import com.lwjandlyw.personalfinance.response.Response;
import com.lwjandlyw.personalfinance.service.IERecordService;
import com.lwjandlyw.personalfinance.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MainController
 * @Description
 * @date 2024/6/11 0:05
 * @Author Squareroot_2
 */
@RestController
@Controller
@CrossOrigin
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    IERecordService recordService;

    @PostMapping("/register")
    public Response register(@RequestBody UserBody userBody) {
        int register = userService.registration(userBody);

        String message;
        if (register >= 0) {
            message = "注册成功";
        } else if (register == -1) {
            message = "用户名重复";
        } else
            message = "未知错误";
        return new Response(register, message, null);
    }

    @PostMapping("/login")
    public Response login(@RequestBody UserBody userBody,
                          HttpSession session,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        int login = userService.login(userBody);
        int code;
        String message;
        if (login >= 0) {
            code = 0;
            Integer uid = login;
            message = "登录成功。";
            Cookie uidCookie = new Cookie("user_id", uid.toString());
            uidCookie.setMaxAge(24 * 60 * 60);
            uidCookie.setPath(request.getContextPath());
            response.addCookie(uidCookie);
        } else {
            code = login;
            message = switch (login) {
                case -1 -> "用户不存在。";
                case -2 -> "密码错误。";
                default -> "未知错误。";
            };
        }
        return new Response(code, message, null);
    }

    @PostMapping("/record")
    public Response record(@RequestBody IERecordBody recordBody,
                           HttpSession session,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        int code;
        String message;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        List<Cookie> userIdCookies = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("user_id"))
                .toList();
        if (userIdCookies.isEmpty()) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        int user_id = Integer.parseInt(userIdCookies.get(0).getValue());
        int record = recordService.insert(recordBody, user_id);
        if (record > 0) {
            code = 0;
            message = "添加成功";
        } else {
            code = -2;
            message = "添加失败";
        }
        return new Response(code, message, null);
    }

    @GetMapping("/record/view/{type}/{tag}")
    public Response viewRecord(@PathVariable("type") String type,
                               @PathVariable("tag") String tag,
                               HttpSession session,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        int code;
        String message;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        List<Cookie> userIdCookies = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("user_id"))
                .toList();
        if (userIdCookies.isEmpty()) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        int user_id = Integer.parseInt(userIdCookies.get(0).getValue());
        if (tag.equals("all")) {
            switch (type) {
                case "all" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUserid(user_id);
                    return new Response(code, message, data);
                }
                case "income" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUserid(user_id).stream()
                            .filter(IERecord::isIncome)
                            .toList();
                    return new Response(code, message, data);
                }
                case "expenditure" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUserid(user_id).stream()
                            .filter(record -> !record.isIncome())
                            .toList();
                    return new Response(code, message, data);
                }
                default -> {
                    code = -2;
                    message = "type 错误";
                    return new Response(code, message, null);

                }
            }
        } else {
            int itag = Integer.parseInt(tag);
            switch (type) {
                case "all" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUseridDivideByTag(user_id, itag);
                    return new Response(code, message, data);
                }
                case "income" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUseridDivideByTag(user_id, itag)
                            .stream()
                            .filter(IERecord::isIncome)
                            .toList();
                    return new Response(code, message, data);
                }
                case "expenditure" -> {
                    code = 0;
                    message = "查询成功";
                    List<IERecord> data = recordService.getRecordByUseridDivideByTag(user_id, itag)
                            .stream()
                            .filter(record -> !record.isIncome())
                            .toList();
                    return new Response(code, message, data);
                }
                default -> {
                    code = -2;
                    message = "type 错误";
                    return new Response(code, message, null);
                }
            }
        }
    }
}
