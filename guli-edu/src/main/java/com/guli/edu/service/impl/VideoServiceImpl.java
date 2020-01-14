package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.edu.client.VodClient;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideById(String videoId) {
        //应删除阿里云视频点播上的视频
        //先获取video信息 再获取阿里云视频videoSourceId
        Video video = baseMapper.selectById(videoId);
        String videoSourceId = video.getVideoSourceId();

        //先删除阿里云视频
        if (!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeByVideoSourceId(videoSourceId);
        }

        //删除video信息
        baseMapper.deleteById(videoId);
    }

    @Override
    public void removeVideByVideoSourceId(String videoSourceId,Video video) {
        //先删除云端视频
        vodClient.removeByVideoSourceId(videoSourceId);

        if (!StringUtils.isEmpty(video.getVideoSourceId())){
            //把videoSourceId及videoOriginalName字段改为空
            video.setVideoSourceId("");
            video.setVideoOriginalName("");
            baseMapper.updateById(video);
        }

    }
}
