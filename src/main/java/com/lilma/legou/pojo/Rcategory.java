package com.lilma.legou.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rcategory {
    //种类id
    private Integer categoryid;
    //种类名称
    private String categoryname;
    //种类销售数量
    private Integer categorynum;
    //种类销售额
    private BigDecimal categorysum;
    //种类比率
    private Double ratio;
}
