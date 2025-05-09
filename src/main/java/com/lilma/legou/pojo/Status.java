package com.lilma.legou.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Status {
    //商品id
    private Integer goodsid;
    //商品名称
    private String goodsname;
    //商品图片
    private String goodspicture;
    //商品库存
    private Integer amount;
    //商品价格
    private BigDecimal goodsprice;
    //商品销售数量
    private Integer goodsnum;
    //商品总价格
    private BigDecimal totalprice;
    //是否畅销
    private Integer isSellWell;
}
