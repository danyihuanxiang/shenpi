package com.ssm.demo.service;


import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.entity.BasicsApprove;
import com.ssm.demo.entity.ResultPostil;

import java.util.List;
import java.util.Map;

public interface ResultPostilService {

    public  boolean save(BasicsApprove approve, String flowName);

    public List<ResultPostil> getPostil(Map map);

    boolean startSave(BasicsApproveDto approveDto);


    boolean updateByApplyId(BasicsApproveDto approveDto);
}
