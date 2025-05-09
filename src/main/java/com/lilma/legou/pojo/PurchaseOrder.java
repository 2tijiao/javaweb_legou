package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseOrder {
    //用户id
    private Integer userId;
    //商品id
    private Integer goodsId;
    //商品名
    private String goodsname;
    //商品价格
    private BigDecimal goodsprice;
    //商品数量
    private Integer goodsnum;
    //总价格
    private BigDecimal totalprice;
    //商品购买时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
