package com.lilma.legou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class BehaviorSequenceCategory {
    //浏览商品的用户id
    public Integer userId;
    //浏览的商品种类id
    public Integer categoryId;
    //对某一商品种类的总浏览时间
    public Long dwellTime;
}
