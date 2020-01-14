package com.guli.vod.controller;

import com.guli.common.vo.Result;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description="阿里云视频点播微服务")
@CrossOrigin //跨域
@RestController
@RequestMapping("/vod/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    @ApiOperation(value = "视频上传")
    public Result uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam MultipartFile file){

            String videoId = videoService.uploadVideo(file);
            return Result.ok().data("videoId",videoId);
    }

    @DeleteMapping("removeByVideoSourceId/{videoSourceId}")
    @ApiOperation(value = "根据videoSourceId删除云端视频")
    public Result removeByVideoSourceId(
            @ApiParam(name = "videoSourceId",value = "云端视频id")
            @PathVariable("videoSourceId") String videoSourceId){

            videoService.removeByVideoSourceId(videoSourceId);
            return Result.ok();
    }

    @ApiOperation(value = "根据vid获取课时视频播放凭证")
    @GetMapping("getPlayAuth/{vid}")
    public Result getPlayAuth(
            @ApiParam(name = "vid", value = "课时vid", required = true)
            @PathVariable("vid") String vid){

        String playAuth = videoService.getPlayAuth(vid);
        return Result.ok().data("playAuth",playAuth);
    }

}
