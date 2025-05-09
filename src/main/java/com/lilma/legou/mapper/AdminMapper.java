package com.lilma.legou.mapper;

import com.lilma.legou.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select * from legou_db.admin where adminname=#{name}")
    Admin findByName(String name);

    @Select("select * from legou_db.saler")
    Saler[] getSaler();

    @Insert("insert into legou_db.saler (salername, pwd, categoryid) values (#{salername},#{pwd},#{categoryid}) ")
    void addSaler(Saler saler);

    @Delete("delete from legou_db.saler where id=#{id}")
    void delSaler(Integer id);

    @Update("update legou_db.saler set pwd=#{pwd} where id=#{id}")
    void modifyPwd(@Param("id")Integer id, @Param("pwd")String pwd);

    @Select("SELECT g.goodsname, SUM(o.goodsnum) as goodsnum, SUM(o.totalprice) as totalPrice, g.price as goodsprice " +
            "FROM saler s " +
            "JOIN goods g ON s.categoryid = g.categoryid " +
            "JOIN `order` o ON g.id = o.goodsid " +
            "WHERE s.id = #{id} "+
            "GROUP BY g.goodsname,g.price")
    Order[] getSales(Integer id);

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
            "        goodsid, " +
            "        SUM(goodsnum) AS goodsnum, " +
            "        SUM(totalprice) AS totalprice " +
            "    FROM `order` " +
            "    GROUP BY goodsid " +
            ") ao ON g.id = ao.goodsid " +
            "CROSS JOIN (" +
            "    SELECT " +
            "        AVG(goodsnum) AS avg_sold " +
            "    FROM (" +
            "        SELECT " +
            "            goodsid, " +
            "            SUM(goodsnum) AS goodsnum " +
            "        FROM `order` " +
            "        GROUP BY goodsid " +
            "    ) sub " +
            ") aso " +
            "ORDER BY totalprice DESC;")
    Status[] getGoods();

    @Update("UPDATE goods SET amount = #{amount} WHERE id = #{goodsid}")
    void addAmount(@Param("goodsid") Integer goodsid, @Param("amount") Integer amount);

    @Select("SELECT " +
            "    c.categoryname, " +
            "    cgs.categoryid, " +
            "    cgs.categorynum, " +
            "    cgs.categorysum, " +
            "    IFNULL(cgs.categorysum / ts.overall_sum, 0) AS ratio " +
            "FROM (" +
            "    SELECT " +
            "        c.id AS categoryid, " +
            "        COALESCE(SUM(o.goodsnum), 0) AS categorynum, " +
            "        COALESCE(SUM(o.totalprice), 0) AS categorysum " +
            "    FROM " +
            "        categorys c " +
            "    LEFT JOIN " +
            "        goods g ON c.id = g.categoryid " +
            "    LEFT JOIN " +
            "        `order` o ON g.id = o.goodsid " +
            "    GROUP BY " +
            "        c.id " +
            ") cgs " +
            "JOIN " +
            "    categorys c ON cgs.categoryid = c.id " +
            "CROSS JOIN (" +
            "    SELECT " +
            "        SUM(categorysum) AS overall_sum " +
            "    FROM (" +
            "        SELECT " +
            "            c.id AS categoryid, " +
            "            COALESCE(SUM(o.goodsnum), 0) AS categorynum, " +
            "            COALESCE(SUM(o.totalprice), 0) AS categorysum " +
            "        FROM " +
            "            categorys c " +
            "        LEFT JOIN " +
            "            goods g ON c.id = g.categoryid " +
            "        LEFT JOIN " +
            "            `order` o ON g.id = o.goodsid " +
            "        GROUP BY " +
            "            c.id " +
            "    ) sub " +
            ") ts;")
    Rcategory[] getRatio();

    @Insert("insert into categorys (categoryname) values (#{categoryname})")
    void addCategorys(String categoryname);

    @Select("select * from categorys")
    Categorys[] getCategorys();

    @Insert("insert into legou_db.adminbigdatalogin(adminid, login_time, ip) VALUES (#{id},now(),#{ip})")
    void loginRecord(@Param("id")Integer id, @Param("ip")String clientIp);

    @Update("update legou_db.adminbigdatalogin set logout_time=now() where adminid=#{id}")
    void logout(Integer id);

    @Insert("insert into legou_db.adminbigdataoperation(operation_time, operation_content, ip, adminname) values (now(),#{content},#{ip},#{adminname})")
    void operateRecord(@Param("adminname")String adminname, @Param("ip")String ip, @Param("content")String content);

    @Select("select salername from legou_db.saler where id=#{id}")
    String findSalerById(Integer id);

    @Select("select goodsname from goods where id=#{goodsid}")
    String findGoodsNameById(Integer goodsid);

    @Select("select location from user where id=#{userid}")
    String findLocById(Integer userid);

    @Select("select `order`.userid,`order`.goodsid,`order`.goodsname,`order`.goodsprice,`order`.goodsnum,`order`.totalprice,`order`.create_time from `order`")
    List<PurchaseOrder> getPurchaseOrder();

    @Select("SELECT u.userid, g.categoryid, u.dwell_time " +
            "FROM userbigdatagoods u " +
            "JOIN goods g ON u.goodsid = g.id " +
            "WHERE u.userid = #{userId}")
    List<BehaviorSequenceCategory> getCateBehavior(Integer userid);

    @Select("select id,goodsprice,goodsnum,totalprice from legou_db.`order`")
    List<AnomalOrder> getAnomalOrder();

    @Select("select categorys.categoryname from categorys where id=#{categoryid}")
    String findCategoryNameById(int categoryid);

    @Select("select goodsid,goodsnum,create_time from legou_db.`order`")
    List<OrderRecord> getOrderRecord();

    @Select("SELECT COUNT(*) FROM goods")
    int getGoodsnum();

    @Select("select id from user")
    List<Integer> getUserids();

    @Select("SELECT " +
            "a.adminname as name, " +
            "l.login_time, " +
            "l.logout_time, " +
            "l.ip " +
            "FROM adminbigdatalogin l " +
            "JOIN admin a ON l.adminid = a.id " +
            "UNION ALL " +
            "SELECT " +
            "s.salername as name, " +
            "l.login_time, " +
            "l.logout_time, " +
            "l.ip " +
            "FROM salerbigdatalogin l " +
            "JOIN saler s ON l.salerid = s.id " +
            "UNION ALL " +
            "SELECT " +
            "u.username as name, " +
            "l.login_time, " +
            "l.logout_time, " +
            "l.ip " +
            "FROM userbigdatalogin l " +
            "JOIN user u ON l.userid = u.id " +
            "ORDER BY login_time ASC")
    BigDataLogin[] getLoginData();

    @Select("SELECT " +
            "s.salername AS name, " +
            "s.operation_time, " +
            "s.operation_content, " +
            "s.ip " +
            "FROM salerbigdataoperation s " +
            "UNION ALL " +
            "SELECT " +
            "a.adminname AS name, " +
            "a.operation_time, " +
            "a.operation_content, " +
            "a.ip " +
            "FROM adminbigdataoperation a " +
            "ORDER BY operation_time ASC")
    OperateData[] getOperateData();

    @Delete("DELETE FROM `order` WHERE id = #{orderid}")
    void deleteOrder(Integer orderid);

    @Delete("delete from legou_db.categorys where id=#{id}")
    void delcategory(Integer id);
}
