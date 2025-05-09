package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    //订单id，唯一标识
    private Integer id;
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
    //商品图片
    private String goodspicture;
    //商品总价格
    private BigDecimal totalprice;
    //商品状态
    private Integer state;
    //商品购买时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
