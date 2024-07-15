package com.lwjandlyw.personalfinance.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName SummaryData
 * @Description
 * @date 2024/7/12 11:53
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryData {
    List<Long> income;      //每日收入，单位为分，为正值
    List<Long> expense;     //每日支出，单位为分，为正值
    long income_sum;        //总收入，单位为分，为正值
    long expense_sum;       //总支出，单位为分，为正值
    List<Expense> expense_pie_chart;
}

