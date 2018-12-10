package com.ssm.demo.mapper;

import com.ssm.demo.entity.CopyMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CopyMessageMapper {

    List<CopyMessage> copyMessagePage(@Param("copyName") String copyName,
                                      @Param("flowName") String flowName,
                                      @Param("captionName") String captionName,
                                      @Param("complete") int complete,
                                      @Param("pageNum") int pageNum,
                                      @Param("pageSize") int pageSize);

    int getCount(@Param("copyName") String copyName,
                 @Param("flowName") String flowName,
                 @Param("captionName") String captionName,
                 @Param("complete") int complete);

    void save(CopyMessage copy);
}
