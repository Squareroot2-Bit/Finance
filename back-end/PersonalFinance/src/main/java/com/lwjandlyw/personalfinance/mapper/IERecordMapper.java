package com.lwjandlyw.personalfinance.mapper;

import com.lwjandlyw.personalfinance.pojo.IERecord;
import com.lwjandlyw.personalfinance.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName IERecordMapper
 * @Description
 * @date 2024/6/10 23:32
 * @Author Squareroot_2
 */
@Mapper
public interface IERecordMapper {
    @Select("""
            select record_id, income, user_id, money, "date", tag, remark
            from record
            where record_id = #{record_id}
            """)
    List<IERecord> selectUserByRecordid(int record_id);

    @Select("""
            select record_id, income, user_id, money, "date", tag, remark
            from record
            where user_id = #{user_id}
            """)
    List<IERecord> selectUserByUserid(int user_id);

    int insert(IERecord record);
}