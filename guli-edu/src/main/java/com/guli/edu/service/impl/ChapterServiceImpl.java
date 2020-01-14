package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.edu.client.VodClient;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.ChapterMapper;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VodClient vodClient;

    @Override
    public void removeChapterById(String id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",id);
        List<Video> videos = videoMapper.selectList(videoQueryWrapper);
        if (!videos.isEmpty()){
            String videoSourceIds = "";
            for (Video video : videos) {
                String videoSourceId = video.getVideoSourceId();
                videoSourceIds += videoSourceId + ",";
            }
            videoSourceIds = videoSourceIds.substring(0,videoSourceIds.length()-1);

            vodClient.removeByVideoSourceId(videoSourceIds);

            //根据章节id删除所有视频
            QueryWrapper<Video> wrapper = new QueryWrapper<>();
            wrapper.eq("chapter_id",id);
            videoMapper.delete(wrapper);
        }

        //根据章节id删除章节
        baseMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getChapterList(String id) {
        List<Map<String , Object>> mapList = new ArrayList<>();

        //根据课程id查询章节
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        wrapper.orderByAsc("sort");
        List<Chapter> chapterList = baseMapper.selectList(wrapper);

        for (Chapter chapter : chapterList) {
            Map<String , Object> videoMap = new HashMap<>();
            videoMap.put("id",chapter.getId());
            videoMap.put("title",chapter.getTitle());
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("chapter_id",chapter.getId());
            videoQueryWrapper.orderByAsc("sort");
            List<Video> videoList = videoMapper.selectList(videoQueryWrapper);
            videoMap.put("childList",videoList);

            mapList.add(videoMap);
        }

        return mapList;
    }
}
