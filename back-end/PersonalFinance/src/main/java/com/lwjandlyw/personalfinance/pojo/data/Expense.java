package com.lwjandlyw.personalfinance.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SummaryData
 * @Description
 * @date 2024/7/13 20:15
 * @Author Squareroot_2
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    long value;
    int tag;
}
