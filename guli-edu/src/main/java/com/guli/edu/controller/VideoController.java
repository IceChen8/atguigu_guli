package com.guli.edu.controller;


import com.guli.common.vo.Result;
import com.guli.edu.entity.Video;
import com.guli.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@RestController
@CrossOrigin
@RequestMapping("/edu/video")
@Api(description = "课时管理")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "新增课时")
    @PostMapping("save")
    public Result save(
            @ApiParam(name = "video", value = "课时对象", required = true)
            @RequestBody Video video){

            videoService.save(video);
            return Result.ok();
    }

    @ApiOperation(value = "根据id获取课时")
    @GetMapping("getById/{videoId}")
    public Result getById(
            @ApiParam(name = "videoId", value = "课时videoId", required = true)
            @PathVariable("videoId") String videoId){

            Video video = videoService.getById(videoId);
            return Result.ok().data("video",video);
    }

    @ApiOperation(value = "修改课时")
    @PutMapping("updateById")
    public Result updateById(
            @ApiParam(name = "video", value = "课时对象", required = true)
            @RequestBody Video video){

            videoService.updateById(video);
            return Result.ok();
    }

    @ApiOperation(value = "根据videoId删除课时信息")
    @DeleteMapping("removeById/{videoId}")
    public Result removeById(
            @ApiParam(name = "videoId", value = "课时videoId", required = true)
            @PathVariable("videoId") String videoId){

            videoService.removeVideById(videoId);
            return Result.ok();
    }

    @ApiOperation(value = "根据VideoSourceId删除课时云端视频")
    @DeleteMapping("removeByVideoSourceId/{videoSourceId}")
    public Result removeByVideoSourceId(
            @ApiParam(name = "videoSourceId", value = "课时VideoSourceId", required = true)
            @PathVariable("videoSourceId") String videoSourceId,

            @RequestBody Video video){

        videoService.removeVideByVideoSourceId(videoSourceId,video);
        return Result.ok().message("云视频删除成功");
    }



}

