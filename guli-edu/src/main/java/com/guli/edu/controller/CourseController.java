package com.guli.edu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.Result;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.Teacher;
import com.guli.edu.entity.vo.CourseInfo;
import com.guli.edu.entity.vo.CourseQuery;
import com.guli.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
@RequestMapping("/edu/course")
@Api(description = "课程中心")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "课本基本信息保存")
    @PostMapping("save")
    public Result save(
            @ApiParam(name = "courseInfo" , value = "课程信息")
            @RequestBody CourseInfo courseInfo
            ){

        String courseId = courseService.saveCourseInfo(courseInfo);
        return Result.ok().data("courseId",courseId);
    }

    @GetMapping("getById/{courseId}")
    @ApiOperation(value = "根据课程id获取")
    public Result getById(
            @ApiParam(name = "courseId" , value = "课程")
            @PathVariable("courseId") String courseId){

              CourseInfo courseInfo = courseService.getCourseInfoById(courseId);
              return Result.ok().data("courseInfo",courseInfo);
    }

    @ApiOperation(value = "分页课程列表（不带条件）")
    @GetMapping("page/{page}/{limit}")
    public Result page(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit){

        Page<Course> pageParam = new Page<>(page, limit);
        courseService.page(pageParam,null);
        //给前端返回什么？
        //总记录数
        //每页显示的结果集
        Map<String, Object> map = new HashMap<>();
        map.put("total",pageParam.getTotal());
        map.put("items",pageParam.getRecords());
        map.put("current",pageParam.getCurrent());
        map.put("pages",pageParam.getPages());
        map.put("hasNext",pageParam.hasNext());
        map.put("hasPrevious",pageParam.hasPrevious());

        return Result.ok().data(map);
    }

    @ApiOperation(value = "分页课程列表")
    @PostMapping("page/{page}/{limit}")
    public Result getPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
            @RequestBody CourseQuery courseQuery){

        Page<Course> pageParam = new Page<>(page, limit);
        courseService.pageList(pageParam,courseQuery);

        return Result.ok().data("rows", pageParam.getRecords()).data("total", pageParam.getTotal());

    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("removeById/{id}")
    public Result removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        courseService.removeCourseInfoById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据courseId获取课程信息")
    @GetMapping("getPublishByCourseId/{courseId}")
    public Result getPublishByCourseId(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String courseId){

            Map<String, Object> publishMap = courseService.getPublishByCourseId(courseId);
            return Result.ok().data("map",publishMap);
    }

    @ApiOperation(value = "根据courseId发布课程")
    @PutMapping("updateCourseStatus/{courseId}")
    public Result updateCourseStatus(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){

            courseService.updateCourseStatus(courseId);
            return Result.ok();
    }

    @ApiOperation(value = "根据课程Id查询课程详细信息")
    @GetMapping("getInfoById/{courseId}")
    public Result getInfoById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){

        Map<String, Object> map = courseService.getInfoById(courseId);
        return Result.ok().data(map);
    }

}

