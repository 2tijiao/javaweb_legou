package com.lilma.legou.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rorder {
    // 商品名称
    private String goodsname;
    // 商品数量
    private Integer goodsnum;
    // 商品总价
    private BigDecimal goodssum;
    // 销售比率
    private Double ratio;
}
