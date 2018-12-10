package com.ssm.demo.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BasicsApproveDto implements Serializable {


    private static final long serialVersionUID = -153670455862338461L;

   private int id;
   private String applyName;//发起人
   private int  applyId;//流程的id
   private String flowName;//是属于哪个流程
   private int approveNode;//要审批的节点
   private int node;//有几次审批

   private int    status;//状态，-1之前人在审批，0准备审批，1已审批
   private String approver;//审批人
   private int approvalResult;//审批结果，0为同意，1为不同意
   private String postil;//审批批注
    private String captionName;//申请标题
    private String common;//要展示的数据

   private String reason;//使用理由

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//开始时间

    private String applyTime;//发起时间
    private String completeTime;//完成时间
    private int complete;//1审批完成，2审批拒绝，3审批中

    private String taskType;//任务类型

    private String[] approvers;//审批人

    private String[] copys;//抄送人

    private String dateTime;//审批时间
}
