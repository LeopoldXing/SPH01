package com.hilda.product.service.impl;

import com.hilda.common.config.minio.annotation.EnableMinio;
import com.hilda.common.config.minio.properties.MinioProperties;
import com.hilda.product.service.FileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    @Override
    public String trademarkPicUpload(MultipartFile file) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();

        //  准备获取到上传的文件路径！
        String url = "";

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if(isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .build());
        }

        //  定义一个文件的名称 : 文件上传的时候，名称不能重复！
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;

        // 使用putObject上传一个文件到存储桶中。
        //  minioClient.putObject("asiatrip","asiaphotos.zip", "/home/user/Photos/asiaphotos.zip");
        minioClient.putObject(
                PutObjectArgs.builder().bucket(minioProperties.getBucketName()).object(fileName).stream(
                        inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        //  System.out.println("/home/user/Photos/asiaphotos.zip is successfully uploaded as asiaphotos.zip to `asiatrip` bucket.");
        //  文件上传之后的路径： http://39.99.159.121:9000/gmall/xxxxxx
        url = minioProperties.getEndpointUrl()+"/"+minioProperties.getBucketName()+"/"+fileName;

        //  将文件上传之后的路径返回给页面！
        return url;
    }

}
