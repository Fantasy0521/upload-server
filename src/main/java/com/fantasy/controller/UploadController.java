package com.fantasy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fantasy.entity.UploadFile;
import com.fantasy.model.Result.Result;
import com.fantasy.service.IUploadFileService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping({ "/common/upload" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "upload")
public class UploadController {

    @Value("${download.path}")
    private String path;

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
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        //这里采取随机生成一个文件名
        //使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + originalFilename;

        String basePath = path;

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
            uploadFile.setSuffix(suffix);
            uploadFile.setType(getFileType(suffix));
            uploadFile.setCreateTime(new Date());
            uploadFile.setCreateUser(1L);
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
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            String fileName = UUID.randomUUID().toString() + originalFilename;
            String basePath = path;
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
                uploadFile.setSuffix(suffix);
                uploadFile.setType(getFileType(suffix));
                uploadFile.setCreateTime(new Date());
                fileNames.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error(originalFilename + "上传失败" + e.getMessage());
            }
        }
        return Result.ok("上传成功", fileNames);
    }

    private String getFileType(String suffix){
        switch (suffix) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "jfif":
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

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    @ApiOperation(value = "download", notes = "2")
    public void download(String name, @RequestParam(required = false) String dir, HttpServletResponse response) {
        try {
            // 设置基础路径
            String basePath = path;

            // 创建文件对象
            File file;
            if (dir == null) {
                file = new File(basePath + name);
            } else {
                file = new File(basePath + dir + File.separator + name);
            }

            // 检查文件是否存在
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            }

            // 输入流, 通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(file);

            // 输出流, 通过输出流将文件写回到浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            // 获取文件的MIME类型
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                // 如果无法确定MIME类型，则默认为application/octet-stream
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);

            // 查询原文件名
            UploadFile uploadFile = uploadFileService.getOne(new LambdaQueryWrapper<UploadFile>().eq(UploadFile::getFileName, name));
            if (uploadFile == null) {
                throw new RuntimeException("查询不到该文件");
            }

            // 如果是图片或者视频则预览而不是下载
            // 根据文件类型决定是预览还是下载
            boolean isPreviewable = contentType.startsWith("image/") || contentType.startsWith("video/");
            String contentDisposition = isPreviewable ? "inline" : "attachment";

            // 设置Content-Disposition头
            response.setHeader("Content-Disposition", contentDisposition + "; filename=\"" + URLEncoder.encode(uploadFile.getOriginFileName(), "UTF-8") + "\"");

            // 将文件内容写入到输出流中
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            // 关闭流
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            // 这里可以处理异常，比如返回一个错误页面或JSON
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "list",notes = "3")
    public Result list(@RequestParam(value = "fileName",required = false) String fileName,
                       @RequestParam(value = "originFileName",required = false) String originFileName,
                       @RequestParam(value = "sort",required = false) Integer sort,
                       @RequestParam(value = "type",required = false) String type
    ){
        List<UploadFile> list = getUploadFileList(fileName, originFileName, sort,type);
        return Result.ok(list);
    }

    @GetMapping("/listFileNames")
    @ApiOperation(value = "listFileNames",notes = "4")
    public Result listFileNames(@RequestParam(value = "fileName",required = false) String fileName,
                                @RequestParam(value = "originFileName",required = false) String originFileName,
                                @RequestParam(value = "sort",required = false) Integer sort,
                                @RequestParam(value = "type",required = false) String type){
        List<UploadFile> list = getUploadFileList(fileName, originFileName, sort,type);
        List<String> collect = list.stream().map(UploadFile::getFileName).collect(Collectors.toList());
        return Result.ok(collect);
    }

    private List<UploadFile> getUploadFileList(String fileName,String originFileName,Integer sort,String type) {
        LambdaQueryWrapper<UploadFile> queryWrapper = new LambdaQueryWrapper<>();
        if (fileName != null) {
            queryWrapper.eq(UploadFile::getFileName,fileName);
        }
        if (originFileName != null) {
            queryWrapper.eq(UploadFile::getOriginFileName,originFileName);
        }
        if (sort != null) {
            if (sort == 1){
                queryWrapper.orderByDesc(UploadFile::getCreateTime);
            }else {
                queryWrapper.orderByAsc(UploadFile::getCreateTime);
            }
        }
        if (type != null) {
            queryWrapper.eq(UploadFile::getType,type);
        }
        return uploadFileService.list(queryWrapper);
    }

    @DeleteMapping("/deleteFile")
    @ApiOperation(value = "deleteFile",notes = "5")
    public Result deleteFile(@RequestParam("fileName")String fileName,@RequestParam("code")String code){
        try {
            UploadFile uploadFile = uploadFileService.getOne(new LambdaQueryWrapper<UploadFile>().eq(UploadFile::getFileName, fileName));
            if (uploadFile == null) {
                return Result.error("文件 " + fileName + " 不存在");
            }
            uploadFileService.removeById(uploadFile.getId());
            // 构建文件路径，这里假设文件位于项目的根目录或某个特定目录下
            String filePath = path + fileName; // 请替换为实际文件路径
            File file = new File(filePath);

            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    return Result.ok("删除文件 " + fileName + " 成功");
                } else {
                    return Result.error("删除文件 " + fileName + " 失败");
                }
            } else {
                return Result.error("文件 " + fileName + " 不存在");
            }
        } catch (Exception e) {
            return Result.error("删除文件过程中发生错误：" + e.getMessage());
        }
    }

}
