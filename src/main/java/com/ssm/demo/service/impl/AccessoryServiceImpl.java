package com.ssm.demo.service.impl;

import com.google.common.io.Files;
import com.ssm.demo.mapper.AccessoryMapper;
import com.ssm.demo.service.AccessoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccessoryServiceImpl implements AccessoryService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccessoryMapper accessoryMapper;

    @Value("${filePath}")
    private String filePath;

    /**
     *附件上传
     * @param files
     * @return
     */
    @Override
    public String uploadFile(List<MultipartFile> files) {

        String middle = null;
        Date date = new Date();
        File file = null;

       String userName="小黄";

        try {
            if (files != null && files.size()>0) {
                middle =new String(filePath.getBytes("ISO8859-1"),"utf-8")  + date.getTime() + userName;
                file = new File(middle);
                if (!file.exists()) {
                    logger.info("没有文件夹开始创建");
                    file.mkdirs();
                }
                for (MultipartFile uploadFile:files) {
                    File newFile = new File(middle+File.separator+uploadFile.getOriginalFilename());
                    Files.write(uploadFile.getBytes(), newFile);
                    accessoryMapper.save(uploadFile.getOriginalFilename(),(date.getTime() + userName+File.separator+uploadFile.getOriginalFilename()),(date.getTime() + userName));
                }
                return date.getTime() + userName;
            }
        } catch (Exception e) {
            logger.info("文件上传出错");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void download(String filePath,String fileName, HttpServletResponse response) {
        try {
            //进行转码
            //  fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
            //得到要下载的全路径名
            //fileName = filePath+fileName;

            //得到要下载的文件
            File file = new File( new String(filePath.getBytes("iso8859-1"),"UTF-8") + File.separator + filePath+File.separator+fileName);
            logger.info("要下载的文件名为{}"+fileName);
            //如果文件不存在
            if(!file.exists()){
                logger.info("下载文件不存在");
            }
          /*
            String[] split = fileName.split("\\\\");
            System.out.println(Arrays.toString(split));
            System.out.println(split[split.length - 1]);
            //处理文件名
            String realname = split[split.length - 1];*/
         // realname=new String(realname.getBytes("iso8859-1"),"UTF-8");
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));//URLEncoder.encode(realname, "UTF-8")
            //读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(file);
            //创建输出流
            OutputStream out = response.getOutputStream();
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while((len=in.read(buffer))>0){
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            //关闭文件输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateByDateName(String dateName, String dateName2) {
        accessoryMapper.updateByDateName(dateName,dateName2);
    }
}
