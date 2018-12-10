package com.ssm.demo.controller;


import com.alibaba.fastjson.JSON;
import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.LawCaseApplyDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.LawCaseApply;
import com.ssm.demo.service.LawCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("lawCase")
public class LawCaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LawCaseService lawCaseService;


    /*
     *用户开始案件申请
     * @param apply 用户请假的参数对象
     * @return
     */

    @RequestMapping(value = "/startPolice", method = RequestMethod.POST)
    @ResponseBody
    public String startPolice(LawCaseApplyDto applyDto) {
           PageTaskDto taskDto=new PageTaskDto();
            if (lawCaseService.startWorkflow(applyDto)) {
                taskDto.setMessage("sucess");
                taskDto.setStatus(1);
                return JSON.toJSONString(taskDto);
            } else {
                logger.info("案件申请失败");
                taskDto.setMessage("案件申请失败，请稍后重新申请");
                taskDto.setStatus(2);
                return JSON.toJSONString(taskDto);
            }
    }


    /**
     * 查询自己全部申请的记录
     *
     * @param pageNum
     * @param pageSize
     * @param userName 申请人
     * @param complete 0没完成,1已完成,2要修改
     * @return
     */

    @RequestMapping(value = "/applyPage", method = RequestMethod.GET)
    @ResponseBody
    public String apply(int pageNum, int pageSize, String userName,
                        @RequestParam(value = "captionName", required = false) String captionName,
                        @RequestParam(value = "taskType", required = false) Integer taskType,
                        @RequestParam(value = "complete", required = false) Integer complete) {
        PageTaskDto<LawCaseApply> lawCaseApplyPage = lawCaseService.applyPage(userName, pageNum, pageSize, complete, captionName, taskType);
        List<LawCaseApply> applyList = lawCaseApplyPage.getRows();
        applyList.forEach(apply ->
                System.out.println(apply)
        );

        return JSON.toJSONString(lawCaseApplyPage);

    }


    /**
     * 查询自己要审批的案件申请
     *
     * @param userName            审批人
     * @param pageNum
     * @param pageSize
     * @param -1之前人在审批，0准备审批，1已审批
     * @return
     */

    @RequestMapping(value = "/approvePage", method = RequestMethod.GET)
    @ResponseBody
    public String approvePage(String userName, int pageNum, int pageSize,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "captionName", required = false) String captionName,
                              @RequestParam(value = "taskType", required = false) Integer taskType) {
        PageTaskDto<LawCaseApply> applyPage = lawCaseService.approvePage(userName, pageNum, pageSize, status, captionName, taskType);
        List<LawCaseApply> applyList = applyPage.getRows();
        applyList.forEach(apply ->
                System.out.println(apply)
        );

        return JSON.toJSONString(applyPage);

    }


    /**
     * 审批人进行审批
     *
     * @param approveDto
     * @return
     */

    @RequestMapping(value = "/signApprove", method = RequestMethod.GET)
    @ResponseBody
    public String signApprove(BasicsApproveDto approveDto) {
        approveDto.setFlowName("案件");
        return JSON.toJSONString(lawCaseService.signApprove(approveDto));

    }


    /***
     * 审批人查看案件详细
     * @param applyId
     * @return
     */

    @GetMapping("/detailApply")
    @ResponseBody
    public String detailApply(int applyId) {
        ApplyDto apply = lawCaseService.detailApply(applyId);
        String loginName = "admin";
        apply.setLoginName(loginName);
        return JSON.toJSONString(apply);
    }


    /**
     * 导出
     */
    @GetMapping("/export")
    public String export(HttpServletResponse response, int id) throws IOException {
        response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                //设置文件头：最后一个参数是设置下载文件名(这里我们叫：张三.pdf)
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("审批" + ".xls", "UTF-8"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            lawCaseService.export(id, out);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出信息失败";
        }
    }


    /**
     * 修改申请
     *
     * @param approve
     * @return
     */
    @PostMapping(value = "/adjustApply")
    @ResponseBody
    public String adjustApply(ApplyDto approve, String[] approvers, String[] copys) {
       // lawCaseService.adjustApply(approve, approvers, copys);
        return JSON.toJSONString("sucess");

    }

    /**
     * 撤销申请
     */
    @GetMapping("/revocation")
    public String revocation(int id) {
        boolean b = lawCaseService.revocation(id);
        if (b) {
            return JSON.toJSONString("sucess");
        } else {
            return JSON.toJSONString("error");
        }

    }


    /**
     * 案件定时
     */
  //@Scheduled(cron = "0/5 * * * * ?")
    public void caseTiming() {
       List<ApplyDto> applys=lawCaseService.LeaveApplyAll(2);
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
