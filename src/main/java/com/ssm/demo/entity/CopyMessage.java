package com.ssm.demo.entity;


import lombok.Data;


@Data

public class CopyMessage {

    private int id;
    private String copyName;//抄送人
    private String applyName;//发起人
    private int  applyId;//流程的id
    private String flowName;//是属于哪个流程
    private int approveId;//最后一个审批人的id,为了更快的获得抄送数据
}
