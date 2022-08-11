package com.gpchen.blog.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class AmazonS3Utils {
    public static  final String url = "https://s3.amazonaws.com/files.myblog/image/";
    @Value("${AmazonS3.accessKey}")
    private String accessKey;
    @Value("${AmazonS3.accessSecretKey}")
    private String secretKey;
    private final static String bucketName = "files.myblog/image";
    public boolean upload(String fileName, MultipartFile file) {
        try {
            amazonS3().putObject(bucketName,fileName,multipartToFile(file,fileName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private AmazonS3 amazonS3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        return   AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }


}
