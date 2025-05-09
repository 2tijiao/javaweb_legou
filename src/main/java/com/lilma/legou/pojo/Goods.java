package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Goods {
    //商品id
    private Integer id;
    //商品名称
    private String goodsname;
    //商品价格
    private BigDecimal price;
    //商品库存
    private Integer amount;
    //商品简介
    private String desc;
    //商品图片路径
    private String picture;
    //商品种类id
    private Integer categoryid;
    //商品创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
