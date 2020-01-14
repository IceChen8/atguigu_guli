package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.vo.CourseInfo;
import com.guli.edu.entity.vo.CourseQuery;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface CourseService extends IService<Course> {

    /**
     * 保存课程信息 返回courseI
     * @param courseInfo
     * @return
     */
    String saveCourseInfo(CourseInfo courseInfo);

    /**
     * 根据课程id获取
     * @param courseId
     * @return
     */
    CourseInfo getCourseInfoById(String courseId);

    /**
     * 分页课程列表
     * @param pageParam
     */
    void pageList(Page<Course> pageParam, CourseQuery courseQuery);

    /**
     * 根据id删除课程
     * @param id
     */
    void removeCourseInfoById(String id);

    /**
     * 根据courseId获取课程信息
     * @param courseId
     * @return
     */
    Map<String,Object> getPublishByCourseId(String courseId);

    /**
     * 根据courseId发布课程
     * @param courseId
     */
    void updateCourseStatus(String courseId);

    /**
     * 根据课程Id查询课程详细信息
     * @param courseId
     * @return
     */
    Map<String,Object> getInfoById(String courseId);
}
