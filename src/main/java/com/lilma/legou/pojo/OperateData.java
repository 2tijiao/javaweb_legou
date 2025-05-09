package com.lilma.legou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperateData {
    //操作用户名
    private String name;
    //操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
    //操作内容
    private String operationContent;
    //ip地址
    private String ip;
}
