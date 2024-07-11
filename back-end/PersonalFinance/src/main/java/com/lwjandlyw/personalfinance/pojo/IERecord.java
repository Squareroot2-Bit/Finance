package com.lwjandlyw.personalfinance.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName IERecord
 * @Description
 * @date 2024/6/10 22:50
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IERecord {
    int record_id;
    boolean income;
    int user_id;
    long money;
    LocalDate date;
    int tag;
    String remark;
}
