package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.mapper.IERecordMapper;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    public List<IERecord> getRecordByUserid(
            int user_id, int tag, LocalDateTime startDate, LocalDateTime endDate) {
        List<IERecord> RecordsByUserid = recordMapper
                .selectUserByUserid(user_id, startDate, endDate);
        if (tag == 0) return RecordsByUserid;
        else return RecordsByUserid.stream()
                .filter(record -> record.getTag() == tag)
                .toList();
    }

    public int insert(IERecordBody recordBody, int user_id) {
        IERecord record = new IERecord();
        record.setIncome(recordBody.getMoney() >= 0);
        record.setUser_id(user_id);
        record.setMoney(recordBody.getMoney());
        record.setTag(recordBody.getTag());
        record.setRemark(recordBody.getRemark());
        return recordMapper.insert(record);
    }
}
