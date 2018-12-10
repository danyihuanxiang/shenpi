package com.ssm.demo.service.impl;


import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.entity.BasicsApprove;
import com.ssm.demo.entity.ResultPostil;
import com.ssm.demo.mapper.ResultPostilMapper;
import com.ssm.demo.service.ResultPostilService;
import com.ssm.demo.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Transactional
@Service
public class ResultPostilServiceImpl implements ResultPostilService {


    @Autowired
    private ResultPostilMapper resultPostilMapper;


    @Override
    public  boolean save(BasicsApprove approve,String flowName){

        //保存审批结果数据到运行流程表
        ResultPostil resultPostil = new ResultPostil();
        resultPostil.setApplyId(approve.getApplyId());
        resultPostil.setApprovalName(approve.getApprover());
        resultPostil.setFlowName(flowName);
        resultPostil.setDateTime(DateTimeUtil.dateToString(new Date()));
        resultPostilMapper.save(resultPostil);
        if (resultPostil.getId()>0){
            return true;
        }
        return  false;
    }

    /**
     * 取出所有审批结果以及批注
     * @param map
     * @return
     */
    @Override
    public List<ResultPostil> getPostil(Map map) {
        return resultPostilMapper.getPostil(map);
    }

    /**
     * 开始就生成审批结果
     * @param approveDto
     * @return
     */
    @Override
    public boolean startSave(BasicsApproveDto approveDto) {
        int  count=0;
        for (String approver: approveDto.getApprovers()) {
            approveDto.setId(0);
            approveDto.setApprover(approver);
            count=resultPostilMapper.startSave(approveDto);
        }
        return  count>0;
    }

    /**
     * 把审批结果以及意见写入到流程开始生成的记录
     * @param approveDto
     * @return
     */
    @Override
    public boolean updateByApplyId(BasicsApproveDto approveDto) {
        approveDto.setDateTime(DateTimeUtil.dateToString(new Date()));
        return    resultPostilMapper.updateByApplyId(approveDto)>0;
    }

}
