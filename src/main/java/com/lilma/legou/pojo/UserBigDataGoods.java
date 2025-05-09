package com.lilma.legou.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserBigDataGoods {
    //id，唯一标识
    private Integer id;
    //用户id
    private Integer userid;
    //商品id
    private Integer goodsid;
    //停留时间
    private Long dwellTime;

}
