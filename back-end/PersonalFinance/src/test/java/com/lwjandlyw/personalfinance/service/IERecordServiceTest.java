package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import com.lwjandlyw.personalfinance.pojo.data.Expense;
import com.lwjandlyw.personalfinance.pojo.data.SummaryData;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    void getRecordByRecordid() {
        for (int record_id = 0; record_id <= 7; record_id++) {
            IERecord record = service.getRecordByRecordid(record_id);
            assert (record != null) == (record_id > 0 && record_id <= 6);
            assert record == null || record.getRecord_id() == record_id;
        }
    }

    @Test
    @Transactional
    void getRecordsByUserid() {
        int tag = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("1900-01-01", formatter);
        LocalDate endDate = LocalDate.parse("2099-12-31", formatter);
        for (int user_id = 1; user_id <= 2; user_id++) {
            List<IERecord> recordByUserid =
                    service.getRecordsByUserid(user_id, tag, startDate, endDate);
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
                new IERecordBody("20240120", 250, 1, "收入2.50元");
        int user_id = 1;
        int tag = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("1900-01-01", formatter);
        LocalDate endDate = LocalDate.parse("2099-12-31", formatter);
        List<IERecord> recordsBeforeInsert =
                service.getRecordsByUserid(user_id, tag, startDate, endDate);
        int insert = service.insert(recordBody, user_id);
        assert insert != 0;
        List<IERecord> recordsAfterInsert =
                service.getRecordsByUserid(user_id, tag, startDate, endDate);
        assert recordsAfterInsert.size() == recordsBeforeInsert.size() + 1;
    }

    @Test
    @Transactional
    void delete() {
        int user_id = 1;
        for (int record_id = 0; record_id <= 7; record_id++) {
            IERecord record = service.getRecordByRecordid(record_id);
            int delete = service.delete(record_id, user_id);
            if (record == null) assert delete == -2;
            else if (record.getUser_id() != user_id) assert delete == -3;
            else if (service.getRecordByRecordid(record_id) == null)
                assert delete == 0;
            else assert delete == -2;
        }
    }

    @Test
    @Transactional
    void summary() {
        int user_id = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse("20240710", formatter);
        LocalDate endDate = LocalDate.parse("20240712", formatter);
        SummaryData summary = service.summary(startDate, endDate, user_id);
        assert summary.getIncome_sum() == 20000;
        assert summary.getExpense_sum() == 20000;
        for (Expense expense : summary.getExpense_pie_chart()) {
            if (expense.getTag() == 2) assert expense.getValue() == 10000;
            if (expense.getTag() == 3) assert expense.getValue() == 10000;
        }
    }

    @Test
    @Transactional
    void input() {
        List<IERecordBody> recordBodyList = new ArrayList<>() {{
            add(new IERecordBody("20240714", 250, 1, "收入2.50元"));
            add(new IERecordBody("20240714", 114, 1, "收入1.14元"));
            add(new IERecordBody("20240714", -514, 2, "支出5.14元"));
        }};
        int user_id = 1;
        int tag = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("1900-01-01", formatter);
        LocalDate endDate = LocalDate.parse("2099-12-31", formatter);
        List<IERecord> recordsBeforeInput =
                service.getRecordsByUserid(user_id, tag, startDate, endDate);
        int input = service.input(recordBodyList, user_id);
        assert input == recordBodyList.size();
        List<IERecord> recordsAfterInput =
                service.getRecordsByUserid(user_id, tag, startDate, endDate);
        assert recordsAfterInput.size() == recordsBeforeInput.size() + input;
    }

    @Test
    @Transactional
    void output() {
        int user_id = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse("19000101", formatter);
        LocalDate endDate = LocalDate.parse("20991231", formatter);
        for (int tag = 0; tag < 8; tag++) {
            List<IERecord> recordsByUserid =
                    service.getRecordsByUserid(user_id, tag, startDate, endDate);
            List<IERecord> output_type0 =
                    service.output(user_id, 0, tag, startDate, endDate);
            assert output_type0.size() == recordsByUserid.size();
            List<IERecord> output_type1 =
                    service.output(user_id, 1, tag, startDate, endDate);
            assert output_type1.size() == recordsByUserid.stream()
                    .filter(IERecord::isIncome)
                    .count();
            List<IERecord> output_type2 =
                    service.output(user_id, 2, tag, startDate, endDate);
            assert output_type2.size() == recordsByUserid.stream()
                    .filter(record -> !record.isIncome())
                    .count();
        }
    }
}