package com.lwjandlyw.personalfinance.controller;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.body.IERecordListBody;
import com.lwjandlyw.personalfinance.body.IntegerBody;
import com.lwjandlyw.personalfinance.body.UserBody;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import com.lwjandlyw.personalfinance.pojo.data.Expense;
import com.lwjandlyw.personalfinance.pojo.data.SummaryData;
import com.lwjandlyw.personalfinance.response.Response;
import com.lwjandlyw.personalfinance.service.IERecordService;
import com.lwjandlyw.personalfinance.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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

    static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Autowired
    UserService userService;
    @Autowired
    IERecordService recordService;
    private static final int MAX_TAG = 8;

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
        Object data;
        String message;
        if (login >= 0) {
            code = 0;
            message = "登录成功。";
            record UserID(Integer user_id) {
            }
            data = new UserID(login);
        } else {
            code = login;
            message = switch (login) {
                case -1 -> "用户不存在。";
                case -2 -> "密码错误。";
                default -> "未知错误。";
            };
            data = null;
        }
        return new Response(code, message, data);
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
    public Response deleteRecord(@RequestBody IntegerBody integerBody,
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
        Integer record_id = integerBody.getRecord_id();
        int delete = recordService.delete(record_id, user_id);
        switch (delete) {
            case 0 -> {
                code = 0;
                message = "删除成功";
            }
            case -2 -> {
                code = -2;
                message = "删除失败";
            }
            case -3 -> {
                code = -3;
                message = "无权限删除";
            }
            default -> {
                code = delete;
                message = "未知错误";
            }
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
            LocalDate startDate = LocalDate.parse(startDateStr, FORMATTER);
            LocalDate endDate = LocalDate.parse(endDateStr, FORMATTER);
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

    @GetMapping("/summary/{start-date}/{end-date}")
    public Response summaryRecord(@PathVariable("start-date") String startDateStr,
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
        LocalDate startDate = LocalDate.parse(startDateStr, FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, FORMATTER);
        code = 0;
        message = "查询成功";
        SummaryData data = recordService.summary(startDate, endDate, user_id);
        return new Response(code, message, data);
    }

    @PostMapping("/input")
    public Response input(@RequestBody IERecordListBody recordListBody,
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
        List<IERecordBody> recordBodyList = recordListBody.getRecordBodyList();
        int input = recordService.input(recordBodyList, user_id);
        if (input > 0) {
            code = 0;
            message = "共" + input + "条数据导入成功";
        } else {
            code = -2;
            message = "数据导入失败";
        }
        return new Response(code, message, null);
    }

    @GetMapping("/output/{type}/{tag}/{start-date}/{end-date}")
    public Response output(@PathVariable("type") Integer type,
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
        LocalDate startDate = LocalDate.parse(startDateStr, FORMATTER);
        LocalDate endDate = LocalDate.parse(endDateStr, FORMATTER);
        List<IERecord> output = recordService.output(user_id, type, tag, startDate, endDate);
        if (output != null) {
            code = 0;
            message = "共" + output.size() + "条数据导出成功";
            Object data = output;
            return new Response(code, message, data);
        } else {
            code = -2;
            message = "导出失败";
            return new Response(code, message, null);
        }
    }

    int verifyingLogin(HttpServletRequest request) {
        // 未登录返回-1
        int user_id = -1;
        if (Collections.list(request.getHeaderNames()).contains("user_id")) {
            try {
                user_id = request.getIntHeader("user_id");
            } catch (NumberFormatException ignore) {
            }
        }
        return user_id;
    }
}
