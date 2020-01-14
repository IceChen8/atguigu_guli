package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.Teacher;
import com.guli.edu.entity.vo.TeacherQuery;
import com.guli.edu.mapper.TeacherMapper;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private CourseService courseService;
    
    @Override
    public void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery) {

        //创建分页条件对象
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        //根据表sort字段排序
        queryWrapper.orderByAsc("sort");

        //如果没有条件查询
        if(teacherQuery == null){
            baseMapper.selectPage(pageParam,queryWrapper);
            return;
        }
        
        //获取条件参数
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }

        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level",level);
        }

        if (!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }

        if (!StringUtils.isEmpty(end)){
            queryWrapper.ge("gmt_create",end);
        }

        baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public Map<String, Object> getTeacherInfoById(String id) {
        Map<String,Object> mapInfo = new HashMap<>();
        
        Teacher teacher = baseMapper.selectById(id);

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<Course> courseList = courseService.list(wrapper);

        mapInfo.put("teacher",teacher);
        mapInfo.put("courseList",courseList);

        return mapInfo;
    }
}
