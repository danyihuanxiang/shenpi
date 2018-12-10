package com.ssm.demo.dto;


import lombok.Data;

@Data
public class ResourceDto {
    private String resource;//数据资源,1QQ私聊，2QQ群聊，3微信私聊,4微信私聊

    private String  useNumber;//使用个数
}
