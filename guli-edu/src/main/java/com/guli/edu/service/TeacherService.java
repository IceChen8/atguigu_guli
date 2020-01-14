package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.vo.TeacherQuery;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 分页查询（带条件）
     * @param pageParam
     * @param teacherQuery
     */
    void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 根据Id查询讲师基本信息(包含讲师信息及讲师对应的课程)
     * @param id
     * @return
     */
    Map<String,Object> getTeacherInfoById(String id);
}
