package com.lilma.legou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserLabel {
    //用户id
    private Integer userid;
    //用户所属地
    private String location;
    //用户购买力等级
    private String purchasePower;
    //用户感兴趣商品列表
    private List<String> categorys;
}
