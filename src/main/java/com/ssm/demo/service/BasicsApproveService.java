package com.ssm.demo.service;

import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.BasicsApprove;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface BasicsApproveService {

    boolean saveBasicsApprove(BasicsApproveDto approveDto);

    boolean adjustApprove(BasicsApprove approve);

    int signApprove(BasicsApproveDto approveDto);

    PageTaskDto applyPage(String captionName, String flowName, int pageNum, int pageSize, int complete);

    PageTaskDto approvePage(String flowName, int pageNum, int pageSize, int status, String captionName);

    PageTaskDto distributeApprove(BasicsApproveDto approveDto);

    Object detailApply(String flowName, int applyId,int operating);

    String uploadFile(List<MultipartFile> files);

    void download(String filePath, String fileName, HttpServletResponse response);

    PageTaskDto revocation(int applyId, String flowName);

    PageTaskDto copyPage(String flowName, String captionName, int complete, int pageNum, int pageSize);

    PageTaskDto updateApply(ApplyDto applyDto);

    Object endTask(int applyId, String flowName);
}
