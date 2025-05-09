package com.lilma.legou.pojo;

import lombok.Data;

@Data
public class SalerLoginData {
    //销售员信息
    private Saler salerInfo;
    //销售员token
    private String token;
    //构造方法
    public SalerLoginData(Saler salerInfo, String token) {
        this.salerInfo = salerInfo;
        this.token = token;
    }
}
