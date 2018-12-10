package com.ssm.demo.controller;

import com.alibaba.fastjson.JSON;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.TaskCaseManage;
import com.ssm.demo.service.TaskCaseManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/taskCaseMenage")
public class TaskCaseMenageController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TaskCaseManageService  taskCaseManageService;

    @RequestMapping(value = "taskCaseListPage",method = RequestMethod.GET)
    @ResponseBody
    public String TaskCaselistPage(String pageSize,String pageNum){
        int pageSizeint =  Integer.parseInt(pageSize);
        int pageNumint =  Integer.parseInt(pageNum);
        TaskCaseManage taskCaseManage = new TaskCaseManage();
//        taskCaseManage.setTaskName("任务管理1");
        PageTaskDto listtaskCaseManage = taskCaseManageService.selectTaskCaseMenageListPage(pageNumint,pageSizeint,taskCaseManage);
        List<TaskCaseManage> list = listtaskCaseManage.getRows();
        String json = JSON.toJSONString(listtaskCaseManage);
        return json;
    }

    @RequestMapping(value = "updateByPrimary",method = RequestMethod.GET)
    @ResponseBody
    public String updateByPrimaryKeySelective(){
        String msg="";
        try{
            TaskCaseManage taskCaseManage = new TaskCaseManage();
            taskCaseManage.setId(1);
            taskCaseManage.setTaskName("任务管理测试");
            taskCaseManage.setTaskTheme("主题");
            taskCaseManageService.updateByPrimaryKeySelective(taskCaseManage);
            msg="1";
        }catch (Exception e){
            msg="0";
            e.printStackTrace();
        }
        return msg;
    }

    @RequestMapping(value = "insertTaskCase",method = RequestMethod.GET)
    @ResponseBody
    public String insertTaskCase(){
        String msg="";
        try{
        TaskCaseManage taskCaseManage = new TaskCaseManage();
        taskCaseManage.setTaskName("任务名称");
        taskCaseManage.setTaskTheme("任务主题");
        taskCaseManage.setTaskStatus("0");
        taskCaseManage.setTaskType("1");
        taskCaseManage.setDisposalMethod("果断");
        taskCaseManage.setPerson("我");
        taskCaseManageService.insert(taskCaseManage);
        msg="1";
        }catch (Exception e){
            msg="0";
            e.printStackTrace();
        }
        return msg;
    }



}
