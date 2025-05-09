package com.lilma.legou.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Records {
    //用户id
    private Integer userid;
    //商品id
    private Integer goodsid;
    //商品名
    private String goodsname;
    //商品价格
    private BigDecimal goodsprice;
    //商品数量
    private Integer goodsnum;
    //商品总价格
    private BigDecimal totalprice;
    //用户名
    private String username;
}
