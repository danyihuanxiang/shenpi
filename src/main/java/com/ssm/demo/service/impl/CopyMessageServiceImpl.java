package com.ssm.demo.service.impl;

import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.CopyMessage;
import com.ssm.demo.mapper.CopyMessageMapper;
import com.ssm.demo.service.BasicsApproveService;
import com.ssm.demo.service.CopyMessageService;
import com.ssm.demo.service.LawCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CopyMessageServiceImpl implements CopyMessageService {


    @Autowired
    private CopyMessageMapper copyMessageMapper;

    @Autowired
    private BasicsApproveService basicsApproveService;

    @Autowired
    private LawCaseService lawCaseService;


    /**
     * 查看需要抄送的数据
     * 单个类型调用其他方法
     * 全部类型直接查询审批表
     * @param
     * @return
     */
    @Override
    public PageTaskDto copyPersonPage(String flowName, String captionName, int complete,int pageNum,int pageSize) {
        String copyName="王珊珊";
       pageNum=pageNum!=0?pageNum-1:1;
       int count=copyMessageMapper.getCount(copyName,flowName,captionName,complete);
        List<CopyMessage> copyMessages=copyMessageMapper.copyMessagePage(copyName,flowName,captionName,complete, pageNum, pageSize);
        PageTaskDto pageTaskDto =new PageTaskDto();
       pageTaskDto.setRows(copyMessages);
       pageTaskDto.setTotal(count);
       pageTaskDto.setStatus(1);
       pageTaskDto.setMessage("成功");
       return pageTaskDto;
    }


    /**
     * 查看详细
     * @param flowName
     * @param applyid
     * @return
     */
    @Override
    public Object detailApply(String flowName, int applyid) {
       if (flowName.equals("案件")){
           return lawCaseService.detailApply(applyid);
       }/*else if (flowName.equals("请假")){
           return leaveService.detailApply(applyid);
       }*/
       return  null;
    }


    /***
     * 保存信息到抄送人表
     * @param approveDto
     */
    @Override
    public boolean save(BasicsApproveDto approveDto) {
        CopyMessage copy = new CopyMessage();
        copy.setApplyId(approveDto.getApplyId());
        copy.setApplyName(approveDto.getApplyName());
        copy.setApproveId(approveDto.getId());
        copy.setFlowName(approveDto.getFlowName());
        copy.setCopyName(approveDto.getCopys()[0]);
        copyMessageMapper.save(copy);
        for (String copyName:approveDto.getCopys()) {
            if (!copyName.equals(approveDto.getCopys()[0])) {
                copy.setId(0);
                copy.setCopyName(copyName);
                copyMessageMapper.save(copy);
            }
        }

        if (copy.getId() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
