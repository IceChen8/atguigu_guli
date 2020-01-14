package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    /**
     * 上传视频
     * @param file
     * @return
     */
    String uploadVideo(MultipartFile file);

    /**
     * 根据videoSourceId删除云端视频
     * @param videoSourceId
     */
    void removeByVideoSourceId(String videoSourceId);

    /**
     * 根据vid获取课时视频播放凭证
     * @param vid
     * @return
     */
    String getPlayAuth(String vid);
}
