package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.mapper.IERecordMapper;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import com.lwjandlyw.personalfinance.pojo.data.Expense;
import com.lwjandlyw.personalfinance.pojo.data.SummaryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName IERecordService
 * @Description
 * @date 2024/6/10 23:54
 * @Author Squareroot_2
 */
@Service
public class IERecordService {

    @Autowired
    IERecordMapper recordMapper;

    private static final int MIN_TAG = 1;
    private static final int MAX_TAG = 8;

    static DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public IERecord getRecordByRecordid(int record_id) {
        List<IERecord> recordList = recordMapper.selectRecordsByRecordid(record_id);
        if (recordList.size() == 1) return recordList.get(0);
        else return null;
    }

    public List<IERecord> getRecordsByUserid(
            int user_id, int tag, LocalDate startDate, LocalDate endDate) {
        List<IERecord> RecordsByUserid = recordMapper
                .selectRecordsByUserid(user_id, startDate, endDate);
        if (tag == 0) return RecordsByUserid;
        else return RecordsByUserid.stream()
                .filter(record -> record.getTag() == tag)
                .toList();
    }

    public int insert(IERecordBody recordBody, int user_id) {
        IERecord record = new IERecord();
        record.setIncome(recordBody.getMoney() >= 0);
        record.setUser_id(user_id);
        record.setDate(LocalDate.parse(recordBody.getDate(), Formatter));
        record.setMoney(recordBody.getMoney());
        record.setTag(recordBody.getTag());
        record.setRemark(recordBody.getRemark() == null ? "" : recordBody.getRemark());
        int insert = 0;
        try {
            insert = recordMapper.insert(record);
        } catch (UncategorizedSQLException ignore) {
        }
        if (insert == 0) return 0;
        else return record.getRecord_id();
    }

    public int delete(int record_id, int user_id) {
        List<IERecord> recordList = recordMapper.selectRecordsByRecordid(record_id);
        IERecord record = null;
        if (recordList.size() == 1)
            record = recordList.get(0);
        if (record == null) {
            return -2;
        } else if (record.getUser_id() == user_id) {
            int delete = recordMapper.delete(record_id);
            if (delete == 1) return 0;
            else return -2;
        } else return -3;
    }

    public SummaryData summary(LocalDate startDate, LocalDate endDate, int user_id) {
        List<IERecord> recordList =
                recordMapper.selectRecordsByUserid(user_id, startDate, endDate);
        List<Long> income = new ArrayList<>(), expense = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            long incomeDay = recordList.stream()
                    .filter(IERecord::isIncome)
                    .filter(record -> record.getDate().isEqual(finalDate))
                    .map(IERecord::getMoney)
                    .reduce(Long::sum)
                    .orElse(0L);
            income.add(incomeDay);
            long expenseDay = recordList.stream()
                    .filter(record -> !record.isIncome())
                    .filter(record -> record.getDate().isEqual(finalDate))
                    .map(record -> -record.getMoney())
                    .reduce(Long::sum)
                    .orElse(0L);
            expense.add(expenseDay);
        }
        long incomeSum = income.stream().reduce(Long::sum).orElse(0L);
        long expenseSum = expense.stream().reduce(Long::sum).orElse(0L);
        List<Expense> expensePieChart = new ArrayList<>();
        for (int tag = MIN_TAG; tag < MAX_TAG; tag++) {
            int finalTag = tag;
            long value = recordList.stream()
                    .filter(record -> !record.isIncome())
                    .filter(record -> record.getTag() == finalTag)
                    .map(record -> -record.getMoney())
                    .reduce(Long::sum)
                    .orElse(0L);
            if (value > 0) expensePieChart.add(new Expense(value, tag));
        }
        return new SummaryData(income, expense,
                incomeSum, expenseSum, expensePieChart);
    }

    public int input(List<IERecordBody> recordBodyList, int user_id) {
        List<Integer> recordidList = new ArrayList<>();
        boolean success = true;
        for (IERecordBody recordBody : recordBodyList) {
            int insert = insert(recordBody, user_id);
            recordidList.add(insert);
            if (insert <= 0) {
                success = false;
                break;
            }
        }
        if (success) {
            return recordBodyList.size();
        } else {
            recordidList.forEach(record_id -> recordMapper.delete(record_id));
            return 0;
        }
    }

    public List<IERecord> output(
            int user_id, int type, int tag,
            LocalDate startDate, LocalDate endDate) {
        if (type < 0 || type > 2) return null;
        if (tag < 0 || tag > MAX_TAG) return null;
        return recordMapper
                .selectRecordsByUserid(user_id, startDate, endDate)
                .stream()
                .filter(record -> tag == 0 || record.getTag() == tag)
                .filter(record -> type == 0 ||
                                  (type == 1 && record.isIncome()) ||
                                  (type == 2 && !record.isIncome()))
                .toList();
    }

}
