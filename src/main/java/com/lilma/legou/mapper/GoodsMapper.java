package com.lilma.legou.mapper;

import com.lilma.legou.pojo.BehaviorSequenceGoods;
import com.lilma.legou.pojo.Categorys;
import com.lilma.legou.pojo.Goods;
import com.lilma.legou.pojo.OrderRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GoodsMapper {
    @Select("select * from legou_db.categorys")
    Categorys[] getCategorys();

    @Select("select * from goods order by create_time DESC limit 4")
    Goods[] getNewGoods();

    @Select("select * from goods where categoryid=#{categoryid}")
    Goods[] getCategoryGoods(Integer categoryid);

    @Select("select * from goods where id=#{goodsid}")
    Goods getGoodsDetail(Integer goodsid);

    Goods[] search(String goodsname);

    @Insert("insert into legou_db.userbigdatagoods(userid, goodsid, dwell_time) " +
            "VALUES (#{userid},#{goodsid},#{dwelltime})")
    void browsingRecord(@Param("userid")Integer userid, @Param("goodsid")Integer goodsid, @Param("dwelltime")Long dwelltime);


    @Select("select legou_db.userbigdatagoods.userid,legou_db.userbigdatagoods.goodsid," +
            "legou_db.userbigdatagoods.dwell_time from legou_db.userbigdatagoods")
    List<BehaviorSequenceGoods> getBehavior();

    @Select("SELECT * FROM goods ORDER BY RAND() LIMIT 4")
    Goods[] getRandomGoods();
}
