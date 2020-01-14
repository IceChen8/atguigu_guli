package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.Result;
import com.guli.edu.entity.Teacher;
import com.guli.edu.entity.vo.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Api(description = "讲师管理")//该接口文档描述
@RestController
@CrossOrigin //跨域
@RequestMapping("/edu/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    /**
     * 给前端返回什么数据？
     * 前端需要什么数据？
     *
     * {
     *     success:true/false,
     *     message:'成功或者失败的消息',
     *     code:20000, //状态码,
     *     data:{} //返回的数据
     *          此data是一个Map对象：key:value
     *          "list":[
     *              {},
     *              {},
     *              {}
     *          ]
     * }
     *  @PathVariable   : 是根据RestFul风格从Path路径中获取参数，映射占位符中的参数
     *  @RequestParam   ：是URL后的?号后面的参数
     *  post请求
     *  @RequestBody    ：请求体内的json数据转成java对象
     */

    @ApiOperation(value = "所有讲师列表")//该接口描述
    @GetMapping("list")
    public Result list(){
        //int i = 1/0;
        List<Teacher> list = teacherService.list(null);
        return Result.ok().data("list",list);
    }

    /**
     * 讲师删除  写删除的时候记得用逻辑删除插件
     */
    @ApiOperation(value = "讲师删除")
    @DeleteMapping("removeById/{id}")
    public Result removeById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable("id") String id){
        boolean b = teacherService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "讲师新增")
    @PostMapping("save")
    public Result save(@RequestBody Teacher teacher){
        teacherService.save(teacher);
        return Result.ok();
    }

    @ApiOperation(value = "讲师分页查询（不带条件）")
    @GetMapping("page/{page}/{limit}")
    public Result page(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit){

        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam,null);
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

    /**
     * 如果提交的是对象建议使用Post请求
     *
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "讲师分页查询（带条件不带条件都可以）")
    @PostMapping("page/{page}/{limit}")
    public Result pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable("page") Integer page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,

            @ApiParam(name = "teacherQuery", value = "查询条件对象", required = false)
            @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.pageQuery(pageParam,teacherQuery);

        return Result.ok()
                .data("total",pageParam.getTotal())
                .data("rows",pageParam.getRecords());
    }

    /**
     * 根据id查询讲师
     */
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacherById/{id}")
    public Result getTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id){

        Teacher teacher = teacherService.getById(id);
        if (teacher == null){
            throw new GuliException(20002,"没有此讲师信息！");
        }
        return Result.ok().data("teacher",teacher);
    }

    @ApiOperation(value = "根据Id查询讲师基本信息(包含讲师信息及讲师对应的课程)")
    @GetMapping("getTeacherInfoById/{id}")
    public Result getTeacherInfoById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable("id") String id){

        Map<String,Object> mapInfo = teacherService.getTeacherInfoById(id);
        if (mapInfo == null){
            throw new GuliException(20002,"没有此讲师信息！");
        }
        return Result.ok().data(mapInfo);
    }

    /**
     * 根据id修改
     */
    @ApiOperation(value = "修改讲师")
    @PutMapping("update")
    public Result updateById(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher){

        teacherService.updateById(teacher);
        return Result.ok();
    }


}

