package com.ssm.demo.service.impl;


import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.LawCaseApplyDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.*;
import com.ssm.demo.mapper.LawCaseApplyMapper;
import com.ssm.demo.service.BasicsApproveService;
import com.ssm.demo.service.CopyMessageService;
import com.ssm.demo.service.LawCaseService;
import com.ssm.demo.service.ResultPostilService;
import com.ssm.demo.util.DateTimeUtil;
import com.ssm.demo.util.ExcleUtil;
import com.ssm.demo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.ServletOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Service
@Transactional
public class LawCaseApplyServiceImpl implements LawCaseService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public  String formatStr="yyyy-MM-dd HH:mm:ss";

    @Autowired
    LawCaseApplyMapper lawCaseApplyMapper;


    @Autowired
    BasicsApproveService basicsApproveService;

    @Autowired
    ResultPostilService resultPostilService;


    @Autowired
    CopyMessageService copyMessageService;


    /**
     * 案件申请
     * @param applyDto
     * @return
     */
    @Override
    public boolean startWorkflow(LawCaseApplyDto applyDto) {
        applyDto.setUserName("admin");
        applyDto.setApplyTime(DateTimeUtil.dateToString(new Date()));
        applyDto.setComplete(3);//状态为审批中
        //保存案件数据到案件表,把基础信息+审批人+节点信息存入审批表
        if ( lawCaseApplyMapper.save(applyDto)>0) {
            BasicsApproveDto approveDto = new BasicsApproveDto();
            approveDto.setApplyId(applyDto.getId());
            approveDto.setApplyName(applyDto.getUserName());
            approveDto.setFlowName("任务审批");
            approveDto.setCaptionName(applyDto.getCaptionName());
            approveDto.setReason(applyDto.getReason());
            approveDto.setStartTime(applyDto.getStartTime());
            approveDto.setEndTime(applyDto.getEndTime());
            approveDto.setCommon(applyDto.getApproveNumber());
            approveDto.setApplyTime(DateTimeUtil.dateToString(new Date()));
            approveDto.setTaskType(applyDto.getTaskType());
            approveDto.setApprovers(applyDto.getApprovers());
            approveDto.setCopys(applyDto.getCopys());
            if (basicsApproveService.saveBasicsApprove(approveDto)) {
                if(copyMessageService.save(approveDto)){
                    return resultPostilService.startSave(approveDto);
                }

            }
        }
        return  false;
    }



    /*
     * 查询自己申请案件的全部数据
     * @param name 申请人
     * @param pageNum
     * @param pageSize
     * @param complete 0没完成,1已完成,2要修改
     * @return
     */
    @Override
    public PageTaskDto<LawCaseApply> applyPage(String name, int pageNum, int pageSize, Integer complete, String captionName,Integer taskType) {
        pageNum=pageNum!=0?pageNum-1:0;
        List<LawCaseApply> LawCaseApplyList=lawCaseApplyMapper.applyPage(name,pageNum,pageSize,complete,captionName,taskType);
        int count=lawCaseApplyMapper.getApplyCount(name,complete,captionName,taskType);
        PageTaskDto<LawCaseApply> pageTaskDto =new PageTaskDto<>();
        pageTaskDto.setTotal(count);
        pageTaskDto.setRows(LawCaseApplyList);
        logger.info("查询自己申请案件的全部数据为-->"+LawCaseApplyList);
        return pageTaskDto;
    }

    /**
     * 放弃申请
     * @param id
     */
    @Override
    public void adjustApplyById(int id) {
        LawCaseApply apply=new LawCaseApply();
        apply.setId(id);
        apply.setComplete(2);
        lawCaseApplyMapper.updateCompleteById(apply);
    }




    /**
     * 查询自己要审批的案件实例
     * @param name
     * @param pageNum
     * @param pageSize
     * @param status -1之前人在审批，0准备审批，1已审批
     * @return
     */
    @Override
    public PageTaskDto<LawCaseApply> approvePage(String name, int pageNum, int pageSize, Integer status, String  captionName,Integer taskType) {
     //  return  basicsApproveService.approvePage(name,"案件",pageNum,pageSize,status,captionName);
        PageTaskDto pageTaskDto =new PageTaskDto();
        int count=0;
        pageNum=pageNum!=0?pageNum-1:0;
        List<LawCaseApply> approves;
        approves=lawCaseApplyMapper.approvePage(name,"案件",pageNum,pageSize,status,captionName,taskType);
        count=lawCaseApplyMapper.getApproveCOunt(name,"案件",status,captionName,taskType);
        pageTaskDto.setTotal(count);
        pageTaskDto.setRows(approves);
        System.out.println(approves);
        return pageTaskDto;
    }


    /**
     * 审批人进行审批
     * @param approveDto
     * @return
     */
    @Override
    public PageTaskDto signApprove(BasicsApproveDto approveDto) {
        PageTaskDto taskDto=new PageTaskDto();
        //保存审批结果数据到运行流程表,如果修改次数少于1后面不用再出现
        if (!resultPostilService.updateByApplyId(approveDto)){
            taskDto.setMessage("审批可能已经进行修改");
            taskDto.setStatus(2);
            return  taskDto;
        }
        //判断流程结果
        int result = basicsApproveService.signApprove(approveDto);
        LawCaseApply apply=new LawCaseApply();
        apply.setId(approveDto.getApplyId());
        if (result!=3) {
            if (result == 2) {
                apply.setComplete(2);
                lawCaseApplyMapper.updateCompleteById(apply);
                logger.info(approveDto.getFlowName() + "流程,id为" + approveDto.getApplyId() + "实例审批被拒绝");
            }
            //流程走完，修改请假表实例为完成
            if (result == 1) {
                apply.setComplete(1);
                apply.setCompleteTime(DateTimeUtil.dateToString(new Date()));
                lawCaseApplyMapper.updateCompleteById(apply);
                logger.info(approveDto.getFlowName() + "流程,id为" + approveDto.getApplyId() + "实例已经审批完");
            }
            //流程没结束
            if (result == 0) {
                logger.info(approveDto.getFlowName() + "流程,id为" + approveDto.getApplyId() + "实例进入下一轮审批");
            }
            taskDto.setMessage("审批成功");
            taskDto.setStatus(1);
            return  taskDto;
        }
        logger.info("审批失败,结果出错");
        taskDto.setMessage("审批出错");
        taskDto.setStatus(2);
        return  taskDto;
    }



    /**
     * 调整申请
     * @return
     */
    @Override
    public boolean adjustApply(ApplyDto applyDto) {
      return   lawCaseApplyMapper.updateApply(applyDto)>0;

    }


    /**
     * 详细
     * @param id
     * @return
     */
    @Override
    public ApplyDto detailApply(int id) {
        return lawCaseApplyMapper.getApply(id);
    }



    @Override
    public void export(int id, ServletOutputStream out) {
        ApplyDto apply = lawCaseApplyMapper.getApply(id);
        List caption = new LinkedList();
        List applyStr = new LinkedList();
        caption.add("申请内容");
        applyStr.add(apply.getCaptionName());
        caption.add("审批编号");
        applyStr.add(apply.getApproveNumber());
        caption.add("使用理由");
        applyStr.add(apply.getReason());
        String caseContent = apply.getCaseContent();
        List<String> strList = StringUtil.getStrList(caseContent, 40);
        for (String str : strList) {
            caption.add("简要案情");
            applyStr.add(str);
        }
        String dispose = (apply.getDispose() == 1) ? "布控" : "信息核查";
        String date = (apply.getDispose() == 1) ? "三个月" : "一个月";
        String resource = null;
  /*      if (apply.getResource() != 0) {
            if (apply.getResource() == 1) {
                resource = "QQ私聊(" + apply.getUseNumber() + ")个";
            }
            if (apply.getResource() == 2) {
                resource = "QQ群聊(" + apply.getUseNumber() + ")个";
            }
            if (apply.getResource() == 3) {
                resource = "微信私聊(" + apply.getUseNumber() + ")个";
            }
            if (apply.getResource() == 4) {
                resource = "微信私聊(" + apply.getUseNumber() + ")个";
            }
        }*/
        caption.add("初始线索");
        applyStr.add(apply.getClue());
        caption.add("任务类型");
        applyStr.add(apply.getTaskType()+"类型");
        caption.add("处置方式");
        applyStr.add(dispose);
        caption.add("数据资源");
        applyStr.add(resource);
        caption.add("使用期限");
        applyStr.add(date);
        caption.add("审批人");
        String postil = apply.getUserName() + "    " + "发起申请" + "   " + apply.getApplyTime();
        applyStr.add(postil);
        List<ResultPostil> postils = apply.getPostils();
        for (ResultPostil resultPostil : postils) {
            caption.add("审批人");
            String result = (resultPostil.getApprovalResult() == 0) ? "已同意" : "不同意";
            postil =resultPostil.getApprovalName() + "   " + result + "   " + resultPostil.getDateTime();
            applyStr.add(postil);
        }

        String copyName = "";
        List<CopyMessage> copyMessages = apply.getCopyMessages();
        for (CopyMessage copy : copyMessages) {
            copyName = copyName + copy.getCopyName() + "    ";
            caption.add("抄送人");
            applyStr.add(copyName);


            ExcleUtil excleUtil = new ExcleUtil();
            try {
                excleUtil.export(caption, applyStr, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用户销毁申请
     * @param applyId
     * @return
     */
    @Override
    public boolean revocation(int applyId) {
    //   if (basicsApproveService.revocation(applyId,"案件")){
         if (lawCaseApplyMapper.revocation(applyId,DateTimeUtil.dateToString(new Date()) )>0){
             return  true;
         }
   //    }
           return false;

    }

    @Override
    public List<Accessory> getAccessorys(String dateName) {
        return lawCaseApplyMapper.getAccessorys(dateName);
    }

    @Override
    public int updateEndTask(int applyId) {
        return lawCaseApplyMapper.updateEndTask(applyId);
    }


    /**
     * 定时任务
     * @param i
     * @return
     */
    @Override
    public List<ApplyDto> LeaveApplyAll(int i) {

        return null;
    }


}
