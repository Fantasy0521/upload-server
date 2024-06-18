package com.fantasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fantasy.entity.UploadFile;
import com.fantasy.model.Result.Result;
import com.fantasy.service.IUploadFileService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping({ "/common/upload" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "upload")
public class UploadController {

    @Value("${linuxImg.url}")
    private String url;

    @Value("${download.url}")
    private String download_url;

    @Autowired
    private IUploadFileService uploadFileService;

    //文件上传 上传图片
    @PostMapping("upload")
    @ApiOperation(value = "upload",notes = "1")
    public Result upload(MultipartFile file) {
        //获取文件原始名,使用原始名可能出现覆盖问题
        String originalFilename = file.getOriginalFilename();
        //获取后缀
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //这里采取随机生成一个文件名
        //使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + originalFilename;

        String basePath = url;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在,创建
            dir.mkdir();
        }

        try {
            file.transferTo(new File(basePath + fileName));
            //存入upload_file表
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFileName(fileName);
            uploadFile.setOriginFileName(originalFilename);
            uploadFile.setDownloadUrl(download_url + fileName);
            uploadFileService.save(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败" + e.getMessage());
        }
        return Result.ok("上传成功", fileName);
    }

    //文件上传 上传图片
    @PostMapping("uploads")
    @ApiOperation(value = "uploads",notes = "1")
    public Result uploads(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            //获取文件原始名,使用原始名可能出现覆盖问题
            String originalFilename = file.getOriginalFilename();
            //获取后缀
//            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + originalFilename;
            String basePath = url;
            //创建一个目录对象
            File dir = new File(basePath);
            //判断当前目录是否存在
            if (!dir.exists()) {
                //目录不存在,创建
                dir.mkdir();
            }

            try {
                file.transferTo(new File(basePath + fileName));
                //存入upload_file表
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFileName(fileName);
                uploadFile.setOriginFileName(originalFilename);
                uploadFile.setDownloadUrl(download_url + fileName);
                uploadFileService.save(uploadFile);
                fileNames.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error(originalFilename + "上传失败" + e.getMessage());
            }
        }
        return Result.ok("上传成功", fileNames);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    @ApiOperation(value = "download",notes = "2")
    public void download(String name, HttpServletResponse response) {
        try {

            String basePath = url;

            //输入流,通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流,通过输出流将文件同时写会浏览器,在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            //浏览器下载
//            response.setContentType("images/jepg");

            //查询原文件名
            UploadFile uploadFile = uploadFileService.getOne(new LambdaQueryWrapper<UploadFile>().eq(UploadFile::getFileName, name));
            if (uploadFile == null) {
                return;
            }
            //浏览器直接预览不下载
            response.setHeader("Content-Type", "image/jpeg");
            response.setHeader("Content-Disposition", "inline; filename=" +  URLEncoder.encode(uploadFile.getOriginFileName(), "UTF-8"));

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @GetMapping("/list")
    @ApiOperation(value = "list",notes = "3")
    public Result list(){
        List<UploadFile> list = uploadFileService.list();
        return Result.ok(list);
    }

    @GetMapping("/listFileNames")
    @ApiOperation(value = "listFileNames",notes = "4")
    public Result listFileNames(){
        List<UploadFile> list = uploadFileService.list();
        List<String> collect = list.stream().map(UploadFile::getFileName).collect(Collectors.toList());
        return Result.ok(collect);
    }

}
