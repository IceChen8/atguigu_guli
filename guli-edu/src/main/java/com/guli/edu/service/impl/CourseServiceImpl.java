package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import com.guli.edu.entity.vo.CourseInfo;
import com.guli.edu.entity.vo.CourseQuery;
import com.guli.edu.mapper.CourseMapper;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseDescriptionService;
import com.guli.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Autowired
    private ChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfo courseInfo) {
        //先插入course表，获取主键id
        Course course = courseInfo.getCourse();
        CourseDescription courseDescription = courseInfo.getCourseDescription();
        String courseId = course.getId();
        if (!StringUtils.isEmpty(courseId)){
            baseMapper.updateById(course);
            courseDescriptionService.updateById(courseDescription);
        }else {
            baseMapper.insert(course);
            courseId = course.getId();
            //再用获取的courseId插入到描述表，因为主键相同  这里可以service调service
            courseDescription.setId(courseId);
            courseDescriptionService.save(courseDescription);
        }

        return courseId;
    }

    @Override
    public CourseInfo getCourseInfoById(String courseId) {

        CourseInfo courseInfo = new CourseInfo();

        Course course = baseMapper.selectById(courseId);
        courseInfo.setCourse(course);

        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfo.setCourseDescription(courseDescription);

        return courseInfo;
    }

    @Override
    public void pageList(Page<Course> pageParam, CourseQuery courseQuery) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");

        if (courseQuery == null){
            baseMapper.selectPage(pageParam,wrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();

        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }

        if (!StringUtils.isEmpty(subjectId)){
            wrapper.eq("subject_id",subjectId);
        }

        if (!StringUtils.isEmpty(subjectParentId)){
            wrapper.eq("subjectParent_id",subjectParentId);
        }

        if (!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id",teacherId);
        }

        baseMapper.selectPage(pageParam,wrapper);

    }

    @Override
    public void removeCourseInfoById(String id) {
        //应删除Course表和courseDescription表对应信息
        baseMapper.deleteById(id);
        courseDescriptionService.removeById(id);
    }

   @Override
    public Map<String, Object> getPublishByCourseId(String courseId) {
        Map<String,Object> publishMap = baseMapper.getPublishByCourseId(courseId);
        return publishMap;
    }

    @Override
    public void updateCourseStatus(String courseId) {
        //先查再修改
        Course course = baseMapper.selectById(courseId);
        course.setStatus("Normal");

        baseMapper.updateById(course);

    }

    @Override
    public Map<String, Object> getInfoById(String courseId) {
        Map<String, Object> map = new HashMap<>();
        //获取课程的详细信息
        Map<String, Object> courseMap = baseMapper.getInfoById(courseId);
        map.put("course",courseMap);
        //获取课程的大纲
        List<Map<String, Object>> chapterList = chapterService.getChapterList(courseId);
        map.put("chapterList",chapterList);

        return map;
    }

}
