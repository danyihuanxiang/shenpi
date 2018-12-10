package com.ssm.demo.mapper;

import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.LawCaseApplyDto;
import com.ssm.demo.entity.Accessory;
import com.ssm.demo.entity.LawCaseApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LawCaseApplyMapper {

  List<LawCaseApply> applyPage(@Param("userName") String userName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("complete") Integer complete, @Param("captionName") String captionName, @Param("taskType") Integer taskType);

  int getApplyCount(@Param("userName") String userName, @Param("complete") Integer complete, @Param("captionName") String captionName, @Param("taskType") Integer taskType);

  int save(LawCaseApplyDto applyDto);

    ApplyDto getApply(int id);

    int updateApply(ApplyDto applyDto);


    void updateCompleteById(LawCaseApply apply);


    List<LawCaseApply> approvePage(@Param("approver") String name, @Param("flowName") String flowName, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("status") Integer status, @Param("captionName") String captionName, @Param("taskType") Integer taskType);

   int getApproveCOunt(@Param("approver") String name, @Param("flowName") String flowName, @Param("status") Integer status, @Param("captionName") String captionName, @Param("taskType") Integer taskType);

    int revocation(@Param("id") int applyId, @Param("completeTime") String completeTime);

    List<Accessory> getAccessorys(String dateName);

    int updateEndTask(int applyId);
}
