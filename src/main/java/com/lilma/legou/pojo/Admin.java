package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Admin {
    //管理员id，唯一标识
    private Integer id;
    //管理员用户名
    private String adminname;
    //管理员密码
    @JsonIgnore
    private String pwd;
}
