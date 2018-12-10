package com.ssm.demo.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AccessoryService {
    String uploadFile(List<MultipartFile> files);

    void download(String filePath, String fileName, HttpServletResponse response);

    void updateByDateName(String dateName, String dateName2);
}
