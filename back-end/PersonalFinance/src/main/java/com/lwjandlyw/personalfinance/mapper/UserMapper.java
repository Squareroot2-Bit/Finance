package com.lwjandlyw.personalfinance.mapper;

import com.lwjandlyw.personalfinance.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description
 * @date 2024/6/10 22:56
 * @Author Squareroot_2
 */

@Mapper
public interface UserMapper {
    @Select("""
            select user_id, username, password, nickname 
            from user 
            where user_id = #{user_id};
            """)
    List<User> selectUserByUserid(int user_id);
    @Select("""
            select user_id, username, password, nickname 
            from user 
            where username = #{username};
            """)
    List<User> selectUserByUsername(String username);

    int insert(User user);
}
