package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.validation.constraints.Email;


@Data
public class User {
    //用户id，唯一标识
    private Integer id;
    //用户名
    private String username;
    //用户密码
    @JsonIgnore
    private String pwd;
    //用户邮箱
    @Email
    private String email;
    //用户年龄
    private Integer age;
    //用户地区
    private String location;



}
