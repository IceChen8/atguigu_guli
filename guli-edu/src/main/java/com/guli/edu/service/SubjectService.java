package com.guli.edu.service;

import com.guli.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 上传excel表格
     * @param file
     * @return
     */
    List<String> importExcl(MultipartFile file);

    /**
     * 分类列表的获取
     * @return
     */
    List<Map<String,Object>> getList();

    /**
     * 删除课程分类
     * @param id
     */
    void removeSubjectById(String id);

    /**
     * 新增分类
     * @param subject
     * @return
     */
    boolean saveLevel(Subject subject);

    /**
     * 根据parentId获取分类列表
     * @param parentId
     * @return
     */
    List<Subject> getSubjectListByParentId(String parentId);
}
