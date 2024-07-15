package com.lwjandlyw.personalfinance.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName IERecordListBody
 * @Description
 * @date 2024/7/12 16:10
 * @Author Squareroot_2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IERecordListBody {
    List<IERecordBody> recordBodyList;
}
