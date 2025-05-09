package com.lilma.legou.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnomalOrder {
    // 订单ID（用于标识异常订单）
    private Integer id;
    // 商品单价
    private BigDecimal goodsprice;
    // 购买数量
    private Integer goodsnum;
    // 订单总价
    private BigDecimal totalprice;
}
