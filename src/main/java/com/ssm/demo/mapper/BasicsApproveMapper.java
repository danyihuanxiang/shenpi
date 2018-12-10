package com.ssm.demo.mapper;


import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.entity.BasicsApprove;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasicsApproveMapper{

    void save(BasicsApproveDto approveDto);

    List<BasicsApprove> ApprovePage(@Param("userName") String userName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("status") int status, @Param("flowName") String flowName, @Param("captionName") String captionName);

    int getCount(@Param("userName") String userName, @Param("status") int status, @Param("flowName") String flowName, @Param("captionName") String captionName);


    int  updateStatusByApproveNode(BasicsApprove approve);


    BasicsApprove  getApproverByStatus(BasicsApproveDto approveDto);

    List<BasicsApprove> Applys(@Param("captionName") String captionName, @Param("applyName") String applyName, @Param("flowName") String flowName, @Param("complete") int complete, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    int getApplysCount(@Param("captionName") String captionName, @Param("applyName") String applyName, @Param("flowName") String flowName, @Param("complete") int complete);

    int revocation(@Param("applyId") int applyId, @Param("flowName") String flowName, @Param("applyName") String applyName);

    int revocations(@Param("applyId") int applyId, @Param("flowName") String flowName);

    int updateEndTask(@Param("applyId") int applyId, @Param("flowName") String flowName);
}
