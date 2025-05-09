package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminBigDataLogin {
    //数据id
    private Integer id;
    //用户id
    private Integer adminid;
    //登陆时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;
    //退出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime logoutTime;
    //ip地址
    private String ip;
}
