package com.fantasy;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fantasy.entity.UploadFile;
import com.fantasy.mapper.UploadFileMapper;
import com.fantasy.service.IUploadFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class TestSql {

    @Autowired
    private IUploadFileService uploadFileService;

    @Test
    public void test(){
        List<UploadFile> list = uploadFileService.list();
        for (UploadFile uploadFile : list) {
            String originFileName = uploadFile.getOriginFileName();
            String suffix = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
            uploadFile.setSuffix(suffix);
            uploadFile.setType(getFileType(suffix));
            uploadFile.setCreateTime(new Date());
            uploadFileService.saveOrUpdate(uploadFile);
        }
    }

    private String getFileType(String suffix){
        switch (suffix) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return "图片";
            case "mp4":
            case "avi":
            case "mkv":
            case "flv":
                return "视频";
            case "doc":
            case "docx":
            case "pdf":
            case "txt":
                return "文档";
            case "xlsx":
            case "xls":
                return "Excel";
            case "ppt":
            case "pptx":
                return "PPT";
            case "zip":
            case "rar":
                return "压缩包";
            default:
                return "其他";
        }
    }

}
