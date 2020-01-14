package com.guli.edu.mapper;

import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据courseId获取课程信息
     * @param courseId
     * @return
     */
    Map<String,Object> getPublishByCourseId(String courseId);

    /**
     * 获取课程的详细信息
     * @param courseId
     * @return
     */
    Map<String,Object> getInfoById(String courseId);
}
