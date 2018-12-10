package com.ssm.demo.mapper;

import com.ssm.demo.dto.BasicsApproveDto;
import com.ssm.demo.entity.ResultPostil;

import java.util.List;
import java.util.Map;

public interface ResultPostilMapper {
    void save(ResultPostil resultPostil);
    List<ResultPostil> getPostil(Map map);

    int startSave(BasicsApproveDto approveDto);

    int updateByApplyId(BasicsApproveDto approveDto);
}
