package com.eas.easserver.mapper;

import com.eas.easserver.bean.User;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT uid,name,phone,department,permission,regTime FROM users WHERE uid=#{uid} AND password=#{password}")
    User selectUserByUidAndPassword(Integer uid,String password);
    @Select("SELECT uid,name,phone,department,permission,regTime FROM users WHERE permission <> 'PERMISSION_EAS_ADMINISTRATOR'")
    List<User> selectUsers();
    @Select("SELECT uid,name,phone,department,permission,regTime FROM users WHERE uid=#{uid}")
    User selectUserByUid(Integer uid);
    @Select("SELECT uid,name,phone,department,permission,regTime FROM users WHERE uid=#{uid} AND token=#{token}")
    User selectUserByUidAndToken(Integer uid,String token);
    @Select("SELECT uid FROM users WHERE phone=#{phone}")
    Integer selectUidByPhone(Long phone);
    @Select("SELECT loginTime FROM users WHERE uid=#{uid}")
    Long selectLoginTimeByUid(Integer uid);
    @Select("SELECT token FROM users WHERE uid=#{uid}")
    String selectTokenByUid(Integer uid);
    @Select("SELECT password FROM users WHERE uid=#{uid}")
    String selectPasswordByUid(Integer uid);
    @Update("UPDATE users SET password=#{password} WHERE uid=#{uid}")
    void updatePasswordByUid(Integer uid,String password);
    @Update("UPDATE users SET name=#{name},phone=#{phone},department=#{department},permission=${permission} WHERE uid=#{uid}")
    void updateUser(Integer uid,String name,Long phone,String department,String permission);
    @Update("UPDATE users SET token=#{token},loginTime=#{loginTime} WHERE uid=#{uid}")
    void updateLoginTokenByUid(Integer uid,String token,Long loginTime);
    @Insert("INSERT INTO users(password,name,phone,department,permission,regTime) VALUES(#{password},#{u.name},#{u.phone},#{u.department},#{u.permission},CURDATE())")
    @Options(useGeneratedKeys = true, keyColumn = "u.uid",keyProperty = "u.uid")
    int insertUser(String password,User u);
    @Delete("DELETE FROM users WHERE uid=#{uid}")
    void deleteUser(Integer uid);
}
