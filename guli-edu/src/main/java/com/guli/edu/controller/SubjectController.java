package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.guli.common.vo.Result;
import com.guli.edu.entity.Subject;
import com.guli.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@RestController
@CrossOrigin
@Api(description = "课程分类管理")
@RequestMapping("/edu/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("file/upload")
    @ApiOperation(value = "excel导入")
    public Result upload(
            @ApiParam(name = "file",value = "excel文件",required = true)
                    MultipartFile file){
            List<String> messageList = subjectService.importExcl(file);
            if (messageList.size() == 0){
                return Result.ok().message("文件导入成功");
            }else{
                return Result.error().message("部分数据导入失败").data("messageList",messageList);
            }
    }

    /**
     * 课程分类列表获取
     */
    @GetMapping("getList")
    @ApiOperation(value = "课程分类列表获取")
    public Result getList(){

        List<Map<String, Object>> mapList = subjectService.getList();

        return Result.ok().data("list",mapList);
    }

    /**
     * 删除课程分类：
     * 删除一级分类需要把它下面的所有的节点都删除
     */
    @DeleteMapping("removeById/{id}")
    @ApiOperation(value = "删除课程分类")
    public Result removeById(@PathVariable("id") String id){
        subjectService.removeSubjectById(id);
        return Result.ok();
    }

    @ApiOperation(value = "新增分类")
    @PostMapping("saveLevel")
    public Result saveLevel(
            @ApiParam(name = "subject", value = "课程分类对象", required = true)
            @RequestBody Subject subject){

        boolean flag = subjectService.save(subject);
        //boolean result = subjectService.saveLevel(subject);
        if(flag){
            return Result.ok();
        }else{
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation(value = "根据parentId查询课程分类列表")
    @GetMapping("getSubjectListByParentId/{parentId}")
    public Result getSubjectListByParentId(
            @ApiParam(name = "parentId" , value = "课程分类parentId")
            @PathVariable("parentId") String parentId){

        List<Subject> subjectList = subjectService.getSubjectListByParentId(parentId);
        return Result.ok().data("list", subjectList);
    }
}

