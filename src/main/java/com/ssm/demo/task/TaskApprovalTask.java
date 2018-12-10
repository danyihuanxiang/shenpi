package com.ssm.demo.task;


import com.ssm.demo.service.TaskCaseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskApprovalTask {

    @Autowired
    TaskCaseManageService taskCaseManageService;

    //@Scheduled(cron = "0/5 * * * * ?")
    public void caseTiming() {
              // taskCaseManageService.ApplyIdBy
       // List<ApplyDto> applys=lawCaseService.LeaveApplyAll(2);
  /*      for (ApplyDto apply:applys) {
          String copysName="";
            List<CopyMessage> copyMessages = apply.getCopyMessages();
            for (CopyMessage copyMessage:copyMessages) {
                copysName=copysName+copyMessage.getCopyName()+"-";
            }
            String[] copys = copysName.substring(0, copyMessages.size() - 1).split("-");
           String approveName="";
            List<BasicsApprove> approves = apply.getApproves();
            for (BasicsApprove approve: approves) {
                approveName=approveName+approve.getApprover()+"-";
            }
            String[] approveNames= copysName.substring(0, copyMessages.size() - 1).split("-");
       lawCaseService.startWorkflow(apply);
    }*/
    }
}
