package com.ssm.demo.mapper;


import com.ssm.demo.entity.TaskCaseManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TaskCaseManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskCaseManage record);

    int insertSelective(TaskCaseManage record);

    TaskCaseManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskCaseManage record);

    int updateByPrimaryKey(TaskCaseManage record);

    List<Map> selectTaskCaseMenageListPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("record") TaskCaseManage record);

    int queryToatl(@Param("record") TaskCaseManage record);
}