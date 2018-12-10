package com.ssm.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageTaskDto<T> {
    private int total;
    private List<T> rows;
    private int status;
    private String message;
    private String commonData;
}
