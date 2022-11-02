package com.hilda.product.controller;

import com.hilda.common.result.Result;
import com.hilda.product.service.FileService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Api("文件相关接口")
@RestController
@RequestMapping("/admin/product")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("Minio图片上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file") MultipartFile file) {
        String url = "";

        try {
            url = fileService.trademarkPicUpload(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }

        System.out.println("url:\t"+url);

        return Result.ok(url);
    }

}
