package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Saler {
    //销售员id，唯一标识
    private Integer id;
    //销售员用户名
    private String salername;
    //销售员密码
    @JsonIgnore
    private String pwd;
    //销售员管理的商品类别id
    private Integer categoryid;
}
