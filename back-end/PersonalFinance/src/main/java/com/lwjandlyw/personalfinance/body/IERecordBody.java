package com.lwjandlyw.personalfinance.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName IERecordBody
 * @Description
 * @date 2024/6/10 23:51
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IERecordBody {
    long money;
    int tag;
    String remark;
}
