package com.lilma.legou.mapper;

import com.lilma.legou.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalerMapper {

    @Select("select* from legou_db.saler where salername=#{salername}")
    Saler findByName(String salername);

    Goods[] getGoods(Integer id);

    @Insert("insert into goods (goodsname, `desc`, price,picture,categoryid,amount,create_time) " +
            "values (#{goodsname},#{desc},#{price},#{picture},#{categoryid},#{amount},now()) ")
    void addGoods(Goods goods);

    @Delete("delete from goods where id=#{id}")
    void delGoods(Integer id);

    @Insert("insert into goods (id,goodsname, `desc`, price,picture,categoryid,amount,create_time) " +
            "values (#{id},#{goodsname},#{desc},#{price},#{picture},#{categoryid},#{amount},now()) ")
    void insertTmpGoods(Goods goods);

    @Update("update goods set goodsname=#{goodsname},price=#{price},amount=#{amount},`desc`=#{desc},picture=#{picture} where id=#{id}")
    void updateGoods(Goods goods);

    @Select("SELECT " +
            "o.userid, " +
            "o.goodsid, " +
            "SUM(o.goodsnum) AS goodsnum, " +
            "MAX(o.goodsname) AS goodsname, " +
            "MAX(o.goodsprice) AS goodsprice, " +
            "SUM(o.totalprice) AS totalprice, " +
            "u.username " +
            "FROM `order` o " +
            "JOIN goods g ON o.goodsid = g.id " +
            "JOIN saler s ON g.categoryid = s.categoryid " +
            "JOIN user u ON o.userid = u.id " +
            "WHERE s.id = #{id}  " +
            "GROUP BY o.userid, o.goodsid, u.username")
    Records[] getRecord(Integer id);

    @Select("SELECT " +
            "o.goodsname, " +
            "SUM(o.goodsnum) AS goodsnum, " +
            "SUM(o.totalprice) AS goodssum, " +
            "SUM(o.totalprice) / (" +
            "    SELECT SUM(totalprice) " +
            "    FROM `order` o2 " +
            "    JOIN goods g2 ON o2.goodsid = g2.id " +
            "    JOIN saler s2 ON g2.categoryid = s2.categoryid " +
            "    WHERE s2.id = #{id}" +
            ") AS ratio " +
            "FROM `order` o " +
            "JOIN goods g ON o.goodsid = g.id " +
            "JOIN saler s ON g.categoryid = s.categoryid " +
            "WHERE s.id = #{id} " +
            "GROUP BY o.goodsid, o.goodsname")
    List<Rorder> ratio(Integer id);

    @Select("SELECT " +
            "    g.id AS goodsid, " +
            "    g.goodsname, " +
            "    g.picture AS goodspicture, " +
            "    g.price AS goodsprice, " +
            "    g.amount, " +
            "    COALESCE(ao.goodsnum, 0) AS goodsnum, " +
            "    COALESCE(ao.totalprice, 0) AS totalprice, " +
            "    CASE WHEN COALESCE(ao.goodsnum, 0) > aso.avg_sold THEN 1 ELSE 0 END AS isSellWell " +
            "FROM " +
            "    goods g " +
            "LEFT JOIN (" +
            "    SELECT " +
            "        o.goodsid, " +
            "        SUM(o.goodsnum) AS goodsnum, " +
            "        SUM(o.totalprice) AS totalprice " +
            "    FROM `order` o " +
            "    JOIN goods g ON o.goodsid = g.id " +
            "    JOIN saler s ON g.categoryid = s.categoryid " +
            "    WHERE s.id = #{id} " +
            "    GROUP BY o.goodsid " +
            ") ao ON g.id = ao.goodsid " +
            "CROSS JOIN (" +
            "    SELECT " +
            "        AVG(goodsnum) AS avg_sold " +
            "    FROM (" +
            "        SELECT " +
            "            o.goodsid, " +
            "            SUM(o.goodsnum) AS goodsnum " +
            "        FROM `order` o " +
            "        JOIN goods g ON o.goodsid = g.id " +
            "        JOIN saler s ON g.categoryid = s.categoryid " +
            "        WHERE s.id = #{id} " +
            "        GROUP BY o.goodsid " +
            "    ) sub " +
            ") aso " +
            "JOIN saler s ON g.categoryid = s.categoryid " +
            "WHERE s.id = #{id} " +
            "ORDER BY totalprice DESC;")
    Status[] getStatus(Integer id);

    @Insert("insert into legou_db.salerbigdatalogin(salerid, login_time, ip) VALUES (#{id},now(),#{ip})")
    void loginRecord(@Param("id")Integer id, @Param("ip")String clientIp);

    @Update("update legou_db.salerbigdatalogin set logout_time=now() where salerid=#{id}")
    void logout(Integer id);

    @Insert("insert into legou_db.salerbigdataoperation(operation_time, operation_content, ip, salername) " +
            "values (now(),#{content},#{ip},#{salername})")
    void operateRecord(@Param("salername")String salername, @Param("ip")String ip, @Param("content")String content);

    @Select("select goodsname from goods where id=#{id}")
    String findById(Integer id);
}
