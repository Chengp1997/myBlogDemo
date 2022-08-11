package com.gpchen.blog.controller;

import com.gpchen.blog.model.vo.ErrorCode;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.util.AmazonS3Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private AmazonS3Utils amazonS3Utils;
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称
        String originalFilename= file.getOriginalFilename();
        //获得新的，唯一的文件名称-- randomID.png/jpg...
        String filename = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename,".");

        boolean upload = amazonS3Utils.upload(filename,file);
        if(upload){
            System.out.println(amazonS3Utils.url);
            return Result.success(amazonS3Utils.url+filename);
        }
        return Result.fail(ErrorCode.UPLOAD_FAIL.getCode(), ErrorCode.UPLOAD_FAIL.getMsg());
    }

}
