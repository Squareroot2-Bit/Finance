package com.lwjandlyw.personalfinance.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    int tag;                 //收支类别
    long income;             //收入，单位为分，为正值
    long expenditure;        //支出，单位为分，为负值
    long incomeRate;         //分项收入占总收入比例，单位万分之一
    long expenditureRate;    //分项支出占总支出比例，单位万分之一
}
