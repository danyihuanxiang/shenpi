package com.ssm.demo.service;


import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.LawCaseApplyDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.Accessory;
import com.ssm.demo.entity.LawCaseApply;

import javax.servlet.ServletOutputStream;
import java.util.List;

public interface LawCaseService {
    boolean startWorkflow(LawCaseApplyDto applyDto);


    PageTaskDto<LawCaseApply> approvePage(String name, int pageNum, int pageSize, Integer status, String captionName, Integer taskType);

    PageTaskDto signApprove(BasicsApproveDto approveDto);

    boolean adjustApply(ApplyDto applyDto);


    ApplyDto detailApply(int id);

    PageTaskDto<LawCaseApply> applyPage(String name, int pageNum, int pageSize, Integer complete, String captionName, Integer taskType);

    void adjustApplyById(int id);

    void export(int id, ServletOutputStream out);

    boolean revocation(int applyId);

    List<Accessory> getAccessorys(String dateName);

    int updateEndTask(int applyId);

    List<ApplyDto> LeaveApplyAll(int i);
}
