package com.guli.edu.controller;


import com.guli.common.vo.Result;
import com.guli.edu.entity.Chapter;
import com.guli.edu.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@RestController
@CrossOrigin
@RequestMapping("/edu/chapter")
@Api(description = "课程章节管理")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "新增章节")
    @PostMapping("save")
    public Result save(
            @ApiParam(name = "chapter", value = "章节对象", required = true)
            @RequestBody Chapter chapter){

            chapterService.save(chapter);
            return Result.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("getById/{id}")
    public Result getById(
            @ApiParam(name = "id",value = "章节id",required = true)
            @PathVariable String id){

            Chapter chapter = chapterService.getById(id);
            return Result.ok().data("chapter",chapter);
    }

    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("updateById/{id}")
    public Result updateChapterById(
            @ApiParam(name = "id", value = "章节id",required = true)
            @PathVariable String id,

            @ApiParam(name = "id", value = "章节对象",required = true)
            @RequestBody Chapter chapter){

            chapter.setId(id);
            chapterService.updateById(chapter);

            return Result.ok();
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("removeById/{id}")
    public Result removeChapterById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){

            chapterService.removeChapterById(id);
            return  Result.ok();
    }

    @ApiOperation(value = "根据课程id查询大纲列表")
    @GetMapping("list/{id}")
    public Result getChapterList(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable("id") String id){

            List<Map<String,Object>> mapList = chapterService.getChapterList(id);
            return Result.ok().data("list",mapList);
    }

}

