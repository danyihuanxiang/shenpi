package com.ssm.demo.service;

import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.TaskCaseManage;

public interface TaskCaseManageService {
    void insert(TaskCaseManage taskCaseManage);
    PageTaskDto selectTaskCaseMenageListPage(int pageNum, int pageSize, TaskCaseManage taskCaseManage);
    void updateByPrimaryKeySelective(TaskCaseManage taskCaseManage);

}
