package com.ssm.demo.mapper;

import org.apache.ibatis.annotations.Param;

public interface AccessoryMapper {
    void save(@Param("fileName") String fileName, @Param("filePath") String filePath, @Param("dateName") String dateName);

    void updateByDateName(@Param("dateName") String dateName,@Param("dateName2") String dateName2);
}
