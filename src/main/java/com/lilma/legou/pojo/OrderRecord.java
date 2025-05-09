package com.lilma.legou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderRecord {
    //商品id
    public Integer goodsid;
    //商品数量
    public Integer goodsnum;
    //商品的购买时间
    public LocalDateTime createTime;
}
