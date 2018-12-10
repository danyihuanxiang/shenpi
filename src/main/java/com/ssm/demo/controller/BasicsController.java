package com.ssm.demo.controller;


import com.alibaba.fastjson.JSON;
import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.BasicsApprove;
import com.ssm.demo.service.BasicsApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping(value = "/basicsApprove")
public class BasicsController  {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BasicsApproveService basicsApproveService;



    /**
     * 查询自己全部申请的数据
     * @param pageNum
     * @param pageSize
     * @param flowName 查询什么流程的审批
     * @param complete 审批完成，审批拒绝，审批中
     * @return
     */
    @GetMapping(value = "/applyPage")
    @ResponseBody
    public String  apply(int pageNum,int pageSize,
                         @RequestParam(value = "captionName", required = false) String captionName,
                         @RequestParam(value = "flowName", required = false,defaultValue = "全部")String flowName,
                         int complete){
        PageTaskDto lawCaseApplyPage = basicsApproveService.applyPage(captionName,flowName, pageNum, pageSize,complete);
        List  applyList = lawCaseApplyPage.getRows();
        applyList.forEach(apply->
                System.out.println(apply)
        );

        return  JSON.toJSONString(lawCaseApplyPage);

    }



    /**
     *我的审批
     * @param pageNum
     * @param pageSize
     * @param status 1待我审批，2已审批，3参与审批
     * @return
     */

    @RequestMapping(value = "/approvePage", method = RequestMethod.GET)
    @ResponseBody
    public String approvePage( int pageNum, int pageSize,
                              @RequestParam(value = "captionName", required = false) String captionName,
                              @RequestParam(value = "flowName", required = false,defaultValue = "全部") String flowName,
                              @RequestParam(value = "status") int status) {
        PageTaskDto<BasicsApprove> applyPage = basicsApproveService.approvePage( flowName,pageNum, pageSize, status, captionName);
         if (applyPage==null){
             applyPage.setStatus(500);
             applyPage.setMessage("查询数据失败，请稍后再试");
         }
        List<BasicsApprove> applyList = applyPage.getRows();
        applyList.forEach(apply ->
                System.out.println(apply)
        );

        return JSON.toJSONString(applyPage);

    }


    /**
     * 审批人进行审批
     * @param approveDto
     * @return
     */
    @GetMapping(value = "/signApprove")
    @ResponseBody
    public String signApprove(BasicsApproveDto approveDto){
         return JSON.toJSONString(basicsApproveService.distributeApprove(approveDto));
    }


    /***
     * 查看请求详细
     * @param flowName 什么项目
     * @param applyId  项目id
     * @param operating 什么操作,1为详细，2为修改
     * @return
     */
    @GetMapping("/detailApply")
    @ResponseBody
    public String detailApply(String flowName,int applyId,
                              @RequestParam(value = "operating", required = false,defaultValue = "1")int operating) {
        return JSON.toJSONString( basicsApproveService.detailApply(flowName,applyId,operating));
    }


    /**
     * 文件下载
     * @param filePath
     * @param fileName
     * @param response
     */
    @GetMapping("/downloadFile")
   public void download(  @RequestParam(value = "filePath", required = false)String filePath, String fileName,HttpServletResponse response){
     basicsApproveService.download(filePath,fileName,response);
   }


    /**
     *
     * MultipartFile  files;//上传文件
     * @return
     */
   @PostMapping("/uploadFile")
   @ResponseBody
   public String uploadFile(List<MultipartFile> files){
       PageTaskDto taskDto=new PageTaskDto();
         taskDto.setCommonData(basicsApproveService.uploadFile(files));
       if (taskDto.getCommonData()!=null){
           taskDto.setStatus(1);
           taskDto.setMessage("sucess");
       }else {
           taskDto.setStatus(2);
           taskDto.setMessage("文件上传失败");
       }


       return JSON.toJSONString(taskDto);
   }


    /**
     * 抄送我的
     * @param captionName 标题
     * @param flowName  类型
     * @param complete  状态
     * @return
     */
    @GetMapping("/copyPage")
    @ResponseBody
    public String copyPage(int pageNum, int pageSize,
            @RequestParam(value = "captionName", required = false) String captionName,
                        @RequestParam(value = "flowName", required = false,defaultValue = "全部") String flowName,
                            @RequestParam(value = "complete", required = false,defaultValue = "0")    int complete){

        PageTaskDto taskDto= basicsApproveService.copyPage(flowName,captionName,complete, pageNum,pageSize);
        if (taskDto==null){
            taskDto.setStatus(2);
            taskDto.setMessage("查询失败，请稍后再试");
        }
        return JSON.toJSONString(taskDto);
   }


    /**
     * 撤销申请
     */
    @GetMapping("/revocation")
    @ResponseBody
    public String revocation(int applyId,@RequestParam(value = "flowName", required = false,defaultValue = "任务审批")String flowName) {
        PageTaskDto revocation = basicsApproveService.revocation(applyId, flowName);
        return JSON.toJSONString(revocation);

    }


    /**
     * 修改申请
     * @return
     */
    @PostMapping("/updateApply")
    public String updateApply(ApplyDto applyDto){
        return JSON.toJSONString(basicsApproveService.updateApply(applyDto));
    }


    /**
     * 任务完成
     * 修改审批的任务完成状态
     * @param applyId  申请id
     * @param flowName  申请类型
     * @return
     */
    @GetMapping("/endTask")
    public String endTask(int applyId,String flowName){
        return JSON.toJSONString(basicsApproveService.endTask( applyId,flowName));
    }


}


