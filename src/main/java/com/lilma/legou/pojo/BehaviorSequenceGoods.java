package com.lilma.legou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class BehaviorSequenceGoods {
    //浏览商品的用户id
    public Integer userId;
    //用户浏览的商品id
    public Integer goodsId;
    //用户对某一商品的总浏览时间
    public Long dwellTime;

}
