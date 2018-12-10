package com.ssm.demo.service.impl;

import com.ssm.demo.dto.ApplyDto;
import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.dto.PageTaskDto;
import com.ssm.demo.entity.Accessory;
import com.ssm.demo.entity.BasicsApprove;
import com.ssm.demo.entity.ResultPostil;
import com.ssm.demo.mapper.BasicsApproveMapper;
import com.ssm.demo.service.AccessoryService;
import com.ssm.demo.service.BasicsApproveService;
import com.ssm.demo.service.CopyMessageService;
import com.ssm.demo.service.LawCaseService;
import com.ssm.demo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;



@Transactional
@Service
public class BasicsApproveServiceImpl implements BasicsApproveService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BasicsApproveMapper basicsApproveMapper;



    @Autowired
    private CopyMessageService copyMessageService;

    @Autowired
    private LawCaseService lawCaseService;


    @Autowired
    private AccessoryService accessoryService;


    /**
     * 启动流程时候设置节点顺序
     * @return
     */
    @Override
    public boolean saveBasicsApprove(BasicsApproveDto approveDto) {
        //根据审批人的顺序来设置流程的节点执行顺序
        int node = 1;
        approveDto.setNode(approveDto.getApprovers().length);
        approveDto.setApproveNode(node);
        approveDto.setApprover(approveDto.getApprovers()[0]);
        approveDto.setStatus(0);
        approveDto.setComplete(3);
        basicsApproveMapper.save(approveDto);
        for (String str : approveDto.getApprovers()) {
            if (!str.equals(approveDto.getApprovers()[0])) {
                ++node;
                approveDto.setApproveNode(node);
                approveDto.setId(0);
                approveDto.setStatus(-1);
                approveDto.setApprover(str);
                basicsApproveMapper.save(approveDto);
            }
        }
        if (approveDto.getId() != 0) {
            return true;
        }
        return false;

    }

    /**
     * 调整审批结果
     *
     * @param approve
     * @return
     */
    @Override
    public boolean adjustApprove(BasicsApprove approve) {
        basicsApproveMapper.updateStatusByApproveNode(approve);
        return true;
    }


    /**
     * 根据审批结果不同，修改审批数据，成功进入下一节点，没有就结束任务
     * 如果审批不通过，则让请求者进行修改
     *
     * @param approveDto
     * @return
     */
    @Override
    public int signApprove(BasicsApproveDto approveDto) {

        BasicsApprove newBasicsApprove = basicsApproveMapper.getApproverByStatus(approveDto);
        //判断这条记录现在是不是这个人审批
        if (null != newBasicsApprove
                && !newBasicsApprove.getApprover().equals(approveDto.getApprover())) {
            return 3;
        }
        //不管审批是否拒绝，修改当前审批状态
        newBasicsApprove.setStatus(1);
        newBasicsApprove.setFlowName(approveDto.getFlowName());
        newBasicsApprove.setApplyId(approveDto.getApplyId());
         //判断审批是否同意，同意则为0;
        if (approveDto.getApprovalResult() == 0) {
            //判断流程是否走完，如果没有走完就继续下一个流程
            int approveNode = newBasicsApprove.getApproveNode();
            if (newBasicsApprove.getNode() != approveNode) {
                int count = basicsApproveMapper.updateStatusByApproveNode(newBasicsApprove);
                if (count == 0) {
                    //修改错误
                    return 3;
                }
                approveNode = approveNode + 1;
                newBasicsApprove.setApproveNode(approveNode);
                newBasicsApprove.setStatus(0);
                System.out.println(newBasicsApprove.toString());
              count =basicsApproveMapper.updateStatusByApproveNode(newBasicsApprove);
                return count>0?0:3;
            } else {
                approveDto.setApproveNode(-1);
                basicsApproveMapper.updateStatusByApproveNode(newBasicsApprove);
                return 1;
            }
        } else if (approveDto.getApprovalResult() == 1) {
            //修改全部状态为2，为领导不通过
            newBasicsApprove.setStatus(1);
          int count=basicsApproveMapper.updateStatusByApproveNode(newBasicsApprove);
            System.out.println(count);
            newBasicsApprove.setStatus(-100);
            newBasicsApprove.setComplete(2);
            newBasicsApprove.setApproveNode(-1);
            count= basicsApproveMapper.updateStatusByApproveNode(newBasicsApprove);
            System.out.println(count);
            return 2;
        }
        return 3;

    }


    /**
     * 请求的数据
     * @param flowName
     * @param pageNum
     * @param pageSize
     * @param complete
     * @return
     */
    @Override
    public PageTaskDto applyPage( String captionName,String flowName, int pageNum, int pageSize, int complete) {
        String applyName="admin";
        PageTaskDto pageTaskDto = new PageTaskDto();
        List<BasicsApprove> approves = null;
        int count = 0;
        pageNum = pageNum != 0 ? pageNum - 1 : 0;
        approves = basicsApproveMapper.Applys(captionName,applyName, flowName, complete, pageNum, pageSize);
        count = basicsApproveMapper.getApplysCount(captionName,applyName, flowName, complete);
        pageTaskDto.setTotal(count);
        pageTaskDto.setRows(approves);
        return pageTaskDto;
    }

    /**
     * 审批的数据
     *
     * @param flowName
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageTaskDto approvePage( String flowName, int pageNum, int pageSize, int status, String captionName) {
        String name="王珊珊";
        PageTaskDto pageTaskDto = new PageTaskDto();
        int count = 0;
        pageNum = pageNum != 0 ? pageNum - 1 : 0;
        List<BasicsApprove> approves;
        approves = basicsApproveMapper.ApprovePage(name, pageNum, pageSize, status, flowName, captionName);
        count = basicsApproveMapper.getCount(name, status, flowName, captionName);
        pageTaskDto.setTotal(count);
        pageTaskDto.setRows(approves);
        pageTaskDto.setStatus(200);
        pageTaskDto.setMessage("sucess");
        System.out.println(approves);
        return pageTaskDto;
    }

    /**
     * 进行审批
     *
     * @param approveDto
     * @return
     */
    @Override
    public PageTaskDto distributeApprove(BasicsApproveDto approveDto) {
        approveDto.setApprover("李丹丹");
        PageTaskDto taskDto=new PageTaskDto();
       // String flowName = approve.getFlowName();
       // if (flowName.equals("任务审批")) {
           return lawCaseService.signApprove(approveDto);
           /*  if (lawCaseService.signApprove(approve)){
                 taskDto.setStatus(1);
                 taskDto.setMessage("审批成功");
                 return  taskDto;
             }else {
                 taskDto.setStatus(2);
                 taskDto.setMessage("审批失败,请稍后重新刷新");
                 return taskDto;
             }*/
    /*    } else if (flowName.equals("请假")) {
             if ( leaveService.signApprove(approve)){
                 taskDto.setStatus(1);
                 taskDto.setMessage("审批成功");
                 return  taskDto;
             }
            taskDto.setStatus(2);
            taskDto.setMessage("审批失败,请稍后重新刷新");
            return  taskDto;
        } taskDto.setStatus(2);
        taskDto.setMessage("数据不正确");
        return  taskDto;*/

    }


    /**
     * 审批人查看请求详细
     *
     * @param applyId
     * @param flowName
     * @return
     */
    @Override
    public Object detailApply(String flowName, int applyId,int operating) {
        String login="admin";
        if (flowName!=null) {
            ApplyDto applyDto = lawCaseService.detailApply(applyId);
           List<Accessory>  accessories=lawCaseService.getAccessorys(applyDto.getDateName());
           applyDto.setAccessorys(accessories);
            applyDto.setResourceDtos(StringUtil.resourceSplit(applyDto.getResource()));
            /*判断是否是查询详细，是就审批人只取到正在审批那级*/
            if (operating==1) {
                int count = 0;
                List<ResultPostil> postils = applyDto.getPostils();
                Iterator<ResultPostil> it = postils.iterator();
                while (it.hasNext()) {
                    ResultPostil p = it.next();
                    if (p.getApprovalResult() == -1) {
                        if (count == 0) {
                            ++count;
                            continue;
                        } else {
                            it.remove();
                        }
                    }
                }
            }
            applyDto.setFlowName(flowName);
            applyDto.setLoginName(login);
            System.out.println(applyDto.toString());
            return applyDto;
        } /*else if (flowName.equals("请假")) {
            ApplyDto applyDto = lawCaseService.detailApply(applyId);
            applyDto.setLoginName(login);
            return applyDto;
        }*/
        return null;
    }


    /**
     * 查看批注
     * @param id
     * @return
     */


    /**
     * 文件上传
     *
     * @param files
     * @return
     */
    @Override
    public String uploadFile(List<MultipartFile> files) {
        return accessoryService.uploadFile(files);
    }


    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @Override
    public void download(String filePath,String fileName, HttpServletResponse response) {
        accessoryService.download(filePath,fileName, response);

    }


    /**
     * 审批进行销毁修改状态为申请失败
     *
     * @param applyId
     * @return
     */
    @Override
    public PageTaskDto revocation(int applyId, String flowName) {
        String applyName="admin";
        PageTaskDto taskDto = new PageTaskDto();
        if (/*basicsApproveMapper.revocation(applyId, flowName,applyName) > 0 &&*/  basicsApproveMapper.revocations(applyId, flowName)> 0){
            if (lawCaseService.revocation(applyId)) {
                taskDto.setStatus(1);
                taskDto.setMessage("撤销成功");
                return taskDto;
            }
        } else {
            taskDto.setStatus(2);
            taskDto.setMessage("撤销失败,请稍后再试");
            return taskDto;
        }
        taskDto.setStatus(2);
        taskDto.setMessage("撤销失败,数据不正确");
        return taskDto;
    }




    /**
     * 抄送我的
     * @param flowName
     * @param captionName
     * @param complete
     * @return
     */
    @Override
    public PageTaskDto copyPage(String flowName, String captionName, int complete,int pageNum,int pageSize) {
        return  copyMessageService.copyPersonPage( flowName,  captionName,  complete, pageNum, pageSize);

    }


    /***
     * 修改申请
     * @param applyDto
     * @return
     */
    @Override
    public PageTaskDto updateApply(ApplyDto applyDto) {
        PageTaskDto taskDto=new PageTaskDto();

        if (applyDto.getDateName()!=null && applyDto.getDateName2()!=null){
            accessoryService.updateByDateName(applyDto.getDateName(),applyDto.getDateName2());
        }

        if (applyDto.getFlowName().equals("任务审批")){
            lawCaseService.adjustApply(applyDto);
            String[] approvers = applyDto.getApprovers();
            for (String approver:approvers) {
                
            }
         //   basicsApproveMapper.updateApplyByApplyId()
        }

        return taskDto;
    }

    @Override
    public Object endTask(int applyId, String flowName) {
        PageTaskDto taskDto=new PageTaskDto();
        if (basicsApproveMapper.updateEndTask(applyId,flowName)>0){
            if (flowName.equals("任务审批")){
                if (lawCaseService.updateEndTask(applyId)>0){

                    taskDto.setStatus(1);
                    taskDto.setMessage("任务状态修改成功");
                    return taskDto;
                }
                  taskDto.setStatus(2);
                taskDto.setMessage("任务状态修改失败");
            }
        }
        taskDto.setMessage("参数不正确");
        return taskDto;
    }


}
