package com.guli.edu.service;

import com.guli.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 根据ID删除章节
     */
    void removeChapterById(String id);

    /**
     * 根据课程id查询大纲列表
     * @param id
     * @return
     */
    List<Map<String,Object>> getChapterList(String id);
}
