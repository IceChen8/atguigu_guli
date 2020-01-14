package com.guli.oss.controller;

import com.guli.common.vo.Result;
import com.guli.oss.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss/file")
@CrossOrigin //跨域
@Api(description = "阿里云oss文件管理")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("upload")
    @ApiOperation(value = "文件上传")
    public Result upload(
                    @ApiParam(name = "file", value = "文件", required = true)
                    @RequestParam MultipartFile file ,

                    @ApiParam(name = "host", value = "文件上传路径", required = true)
                    String host){

        String url = uploadService.updoad(file,host);
        return Result.ok().data("url",url);
    }
}
