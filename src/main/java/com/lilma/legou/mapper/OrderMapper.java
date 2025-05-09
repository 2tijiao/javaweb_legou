package com.lilma.legou.mapper;

import com.lilma.legou.pojo.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    @Select("select * from legou_db.`order` where userid=#{userid} and isdelete=0")
    Order[] getOrder(Integer userid);

    @Update("update `order` set state=1 where id=#{id}")
    void changeState(Integer id);

    @Update("update `order` set isdelete=1 where id=#{id}")
    void del(Integer id);
}
