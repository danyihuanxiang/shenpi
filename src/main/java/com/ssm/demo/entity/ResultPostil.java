package com.ssm.demo.entity;


import lombok.Data;

import java.io.Serializable;

@Data
//运行流程批注审批表
public class ResultPostil implements Serializable {
   private static final long serialVersionUID = -6560784566950858887L;

    private int id;
    private String approvalName;//审批人
    private int approvalResult;//审批结果,0为同意，1为不同意
    private String postil;//审批批注
    private int  applyId;//流程的id
    private String flowName;//是属于哪个流程
    private String dateTime;//审批时间


}
