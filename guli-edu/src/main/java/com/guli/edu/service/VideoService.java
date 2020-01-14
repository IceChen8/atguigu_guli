package com.guli.edu.service;

import com.guli.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
public interface VideoService extends IService<Video> {

    /**
     * 根据videoId删除课时信息
     * @param videoId
     */
    void removeVideById(String videoId);

    /**
     * 根据VideoSourceId删除课时云端视频
     * @param videoSourceId
     */
    void removeVideByVideoSourceId(String videoSourceId,Video video);
}
