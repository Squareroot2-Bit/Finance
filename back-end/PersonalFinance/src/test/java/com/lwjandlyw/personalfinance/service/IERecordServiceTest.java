package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName IERecordServiceTest
 * @Description
 * @date 2024/7/11 1:51
 * @Author Squareroot_2
 */
@SpringBootTest
@MapperScan("com.lwjandlyw.personalfinance.mapper")
class IERecordServiceTest {

    @Autowired
    IERecordService service;

    @Test
    @Transactional
    void getRecordByUseridDivideByTag() {
        int tag = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01 00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2099-12-31 23:59:59", formatter);
        for (int user_id = 1; user_id <= 2; user_id++) {
            List<IERecord> recordByUserid =
                    service.getRecordByUserid(user_id, tag, startDate, endDate);
            for (IERecord record : recordByUserid) {
                assert record.getUser_id() == user_id;
                assert record.getTag() == tag;
            }
        }
    }

    @Test
    @Transactional
    void insert() {
        IERecordBody recordBody =
                new IERecordBody(250, 1, "收入2.50元");
        int user_id = 1;
        int tag = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01 00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse("2099-12-31 23:59:59", formatter);
        List<IERecord> recordsBeforeInsert =
                service.getRecordByUserid(user_id, tag, startDate, endDate);
        int insert = service.insert(recordBody, user_id);
        assert insert == 1;
        List<IERecord> recordsAfterInsert =
                service.getRecordByUserid(user_id, tag, startDate, endDate);
        assert recordsAfterInsert.size() == recordsBeforeInsert.size() + 1;
    }
}