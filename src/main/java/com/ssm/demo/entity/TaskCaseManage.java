package com.ssm.demo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskCaseManage implements Serializable {
    private Integer id;

    private String taskName;

    private String taskTheme;

    private String taskType;

    private String disposalMethod;

    private String taskStatus;

    private String startTime;

    private String endTime;

    private String updateTime;

    private String estimatedTime;

    private String person;

    private String back1;

    private String back2;

    private String back3;

    private String back4;

}