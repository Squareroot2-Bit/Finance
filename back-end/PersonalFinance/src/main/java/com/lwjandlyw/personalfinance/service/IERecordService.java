package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.mapper.IERecordMapper;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    static DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public IERecord getRecordByRecordid(int record_id) {
        List<IERecord> recordList = recordMapper.selectRecordsByRecordid(record_id);
        if (recordList.size() == 1) return recordList.get(0);
        else return null;
    }

    public List<IERecord> getRecordByUserid(
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
        record.setRemark(recordBody.getRemark());
        return recordMapper.insert(record);
    }

    public int delete(int record_id) {
        return recordMapper.delete(record_id);
    }


}
