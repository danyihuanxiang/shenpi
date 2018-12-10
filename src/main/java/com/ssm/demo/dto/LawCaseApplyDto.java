package com.ssm.demo.dto;


import com.ssm.demo.entity.Accessory;
import com.ssm.demo.entity.CopyMessage;
import com.ssm.demo.entity.ResultPostil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class LawCaseApplyDto  implements Serializable {


    private static final long serialVersionUID = 8218714421847258705L;

    private int id;

    private String userName;//申请人名字

    private String captionName;//案件标题

    private String approveNumber;//审批编号

    private String reason;//使用理由

    private String caseContent;//简要案情

    private String clue;//初始线索

    private String taskType;//任务类型

    private int dispose;//处置方式，0为布控，1为信息核查

    private String resource;//数据资源,1QQ私聊，2QQ群聊，3微信私聊,4微信私聊,按照格式-进行区分


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//开始时间

    private String applyTime;//发起时间

    private String completeTime;//完成时间

    private int end;//状态是否结束


    private int complete;//0没完成,1已完成,2要修改,3失败


    private String dateName;//对应附件表


    private String[] approvers;//审批人

    private String[] copys;//抄送人


    private List<ResultPostil> postils;//审批结果以及批注

    private List<CopyMessage> copyMessages;//抄送人

    private List<Accessory> accessorys;//附件资料


}
