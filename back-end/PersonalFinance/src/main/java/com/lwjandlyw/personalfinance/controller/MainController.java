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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    static DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
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
        } else if (register == -3) {
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
        int user_id = verifyingLogin(request);
        if (user_id < 0) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
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

    @PostMapping("/record/delete")
    public Response deleteRecord(@RequestParam("record_id") Integer record_id,
                                 HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        int code;
        String message;
        int user_id = verifyingLogin(request);
        if (user_id < 0) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        int delete = recordService.delete(record_id);
        if (delete == 1) {
            code = 0;
            message = "添加成功";
        } else {
            code = -2;
            message = "删除失败";
        }
        return new Response(code, message, null);
    }


    @GetMapping("/record/view/{type}/{tag}/{start-date}/{end-date}")
    public Response viewRecord(@PathVariable("type") Integer type,
                               @PathVariable("tag") Integer tag,
                               @PathVariable("start-date") String startDateStr,
                               @PathVariable("end-date") String endDateStr,
                               HttpSession session,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        int code;
        String message;
        int user_id = verifyingLogin(request);
        if (user_id < 0) {
            code = -1;
            message = "未登录";
            return new Response(code, message, null);
        }
        if (type >= 0 && type <= 2) {
            LocalDateTime startDate =
                    LocalDateTime.parse(startDateStr + "000000", Formatter);
            LocalDateTime endDate =
                    LocalDateTime.parse(endDateStr + "240000", Formatter);
            List<IERecord> temp =
                    recordService.getRecordByUserid(user_id, tag, startDate, endDate);
            code = 0;
            message = "查询成功";
            List<IERecord> data = switch (type) {
                case 0 -> temp;
                case 1 -> temp.stream().filter(IERecord::isIncome).toList();
                case 2 -> temp.stream().filter(record -> !record.isIncome()).toList();
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
            return new Response(code, message, data);
        } else {
            code = -2;
            message = "type 错误";
            return new Response(code, message, null);
        }
    }

    int verifyingLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return -1;
        List<Cookie> userIdCookies = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("user_id"))
                .toList();
        if (userIdCookies.isEmpty()) return -1;
        return Integer.parseInt(userIdCookies.get(0).getValue());
    }
}
