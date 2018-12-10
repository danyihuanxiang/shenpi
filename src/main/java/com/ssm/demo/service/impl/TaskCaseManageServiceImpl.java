package com.ssm.demo.service.impl;

import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.TaskCaseManage;
import com.ssm.demo.mapper.TaskCaseManageMapper;
import com.ssm.demo.service.TaskCaseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TaskCaseManageServiceImpl implements TaskCaseManageService {
    @Autowired
    TaskCaseManageMapper taskCaseManageMapper;

    private PageTaskDto pageTaskDto = new PageTaskDto();
    @Override
    public void insert(TaskCaseManage taskCaseManage){
        taskCaseManageMapper.insert(taskCaseManage);
    }

    @Override
     public PageTaskDto selectTaskCaseMenageListPage(int pageNum, int pageSize,TaskCaseManage taskCaseManage){
        if(pageNum==0 || pageNum==1){
            pageNum=0;
        }else {
            pageNum=pageSize;
            pageSize=pageNum*pageSize;
        }
        List<Map> list = taskCaseManageMapper.selectTaskCaseMenageListPage(pageNum,pageSize,taskCaseManage);
        int total = taskCaseManageMapper.queryToatl(taskCaseManage);
        pageTaskDto.setRows(list);
        pageTaskDto.setTotal(total);
        return pageTaskDto;
    }

    @Override
    public void updateByPrimaryKeySelective(TaskCaseManage taskCaseManage){
        taskCaseManageMapper.updateByPrimaryKeySelective(taskCaseManage);
    }
}
