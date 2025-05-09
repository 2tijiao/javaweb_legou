package com.lilma.legou.mapper;

import com.lilma.legou.pojo.CartList;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CartlistMapper {
    @Select("select * from legou_db.cartlist where userid=#{userid}")
    CartList[] getlist(Integer userid);

    @Delete("delete from legou_db.cartlist where id=#{id}")
    void delCartlist(Integer id);

    @Insert("insert into legou_db.`order`(userid, goodsid, goodsname, goodsprice, goodsnum, totalprice,state,goodspicture,isdelete,create_time) " +
            "VALUES (#{userid}, #{goodsid}, #{goodsname}, #{goodsprice}, #{goodsnum}, #{totalprice},0,#{goodspicture},0,now())")
    void addOrder(CartList c);

    @Insert("INSERT INTO cartlist (userid, goodsid, goodsname, goodsprice, goodspicture, goodsnum, totalprice) " +
            "VALUES (#{userid}, #{goodsid}, " +
            "(SELECT goodsname FROM goods WHERE id = #{goodsid}), " +
            "(SELECT price FROM goods WHERE id = #{goodsid}), " +
            "(SELECT picture FROM goods WHERE id = #{goodsid}), " +
            "#{goodsnum}, " +
            "(SELECT price * #{goodsnum} FROM goods WHERE id = #{goodsid}))")
    void addCartlist(@Param("goodsid")Integer goodsid, @Param("goodsnum")Integer goodsnum, @Param("userid")Integer userid);

    @Select("select user.email from user where id=#{userid}")
    String getemail(Integer userid);

    @Update("UPDATE goods SET amount = amount - #{goodsnum} WHERE id = #{goodsid}")
    void cutAmount(@Param("goodsid")Integer goodsid, @Param("goodsnum")Integer goodsnum);

    @Select("SELECT goodsid, goodsnum FROM legou_db.cartlist WHERE id = #{id}")
    CartList getCartlistInfo(Integer id);

    @Update("UPDATE goods SET amount = amount + #{goodsnum} WHERE id = #{goodsid}")
    void restoreGoodsAmount(@Param("goodsid") Integer goodsid, @Param("goodsnum") Integer goodsnum);

    @Select("select goods.amount from goods where id=#{goodsid}")
    Integer getAmount(Integer goodsid);

    @Select("select cartlist.goodsnum from cartlist where id=#{id}")
    Integer getCartNum(Integer id);

    @Update("update goods set amount=#{amount}+#{cartlistnum}-#{goodsnum} where id=#{goodsid}")
    void newAmount(@Param("goodsid")Integer goodsid,@Param("amount")Integer amount, @Param("cartlistnum")Integer cartlistnum, @Param("goodsnum")Integer goodsnum);
}
