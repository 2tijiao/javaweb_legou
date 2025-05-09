package com.lilma.legou.mapper;

import com.lilma.legou.pojo.BehaviorSequenceGoods;
import com.lilma.legou.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select* from legou_db.user where username=#{name}")
    User findByName(String name);


    @Insert("insert into legou_db.user(username, pwd, email, age, location) " +
            "values (#{username},#{pwd},#{email},#{age},#{location})")
    void register(@Param("username")String username, @Param("pwd")String pwd, @Param("email")String email, @Param("age")Integer age, @Param("location")String location);

    @Delete("delete from user where id=#{id}")
    void cancel(Integer id);

    @Insert("insert into legou_db.userbigdatalogin(userid, login_time, ip) VALUES (#{id},now(),#{ip})")
    void loginRecord(@Param("id")Integer id, @Param("ip")String clientIp);

    @Update("update legou_db.userbigdatalogin set logout_time=now() where userid=#{id}")
    void logout(Integer id);

}
