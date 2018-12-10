package com.ssm.demo.service;


import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.PageTaskDto;

public interface CopyMessageService {

    PageTaskDto copyPersonPage(String flowName, String captionName, int complete, int pageNum, int pageSize);

    Object detailApply(String flowName, int applyid);

    boolean save(BasicsApproveDto approveDto);
}
