package com.lwjandlyw.personalfinance.service;

import com.lwjandlyw.personalfinance.body.IERecordBody;
import com.lwjandlyw.personalfinance.mapper.IERecordMapper;
import com.lwjandlyw.personalfinance.pojo.IERecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<IERecord> getRecordByUserid(int user_id) {
        return recordMapper.selectUserByUserid(user_id);
    }

    public List<IERecord> getRecordByUseridDivideByIncome(int user_id, boolean is_income) {
        return recordMapper.selectUserByUserid(user_id)
                .stream()
                .filter(record -> record.isIncome() == is_income)
                .toList();
    }

    public List<IERecord> getRecordByUseridDivideByTag(int user_id, int tag) {
        return recordMapper.selectUserByUserid(user_id)
                .stream()
                .filter(record -> record.getTag() == tag)
                .toList();
    }

    public int insert(IERecordBody recordBody,int user_id) {
        IERecord record = new IERecord();
        record.setIncome(recordBody.getMoney() >= 0);
        record.setUser_id(user_id);
        record.setMoney(recordBody.getMoney());
        record.setTag(recordBody.getTag());
        record.setRemark(recordBody.getRemark());
        return recordMapper.insert(record);
    }
}
