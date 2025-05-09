package com.lilma.legou.pojo;

import lombok.Data;

@Data
public class AdminLoginData {
    //销售员信息
    private Admin adminInfo;
    //销售员token
    private String token;
    //构造方法
    public AdminLoginData(Admin adminInfo, String token) {
        this.adminInfo = adminInfo;
        this.token = token;
    }
}
