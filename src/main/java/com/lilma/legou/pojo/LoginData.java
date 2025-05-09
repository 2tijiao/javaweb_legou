package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class LoginData {
    //用户信息
    private User userInfo;
    //用户token
    private String token;
    //构造方法
    public LoginData(User userInfo, String token) {
        this.userInfo = userInfo;
        this.token = token;
    }
}
