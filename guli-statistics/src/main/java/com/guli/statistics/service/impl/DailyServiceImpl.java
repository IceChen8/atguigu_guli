package com.guli.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.statistics.client.UcenterClient;
import com.guli.statistics.entity.Daily;
import com.guli.statistics.mapper.DailyMapper;
import com.guli.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author huaan
 * @since 2020-01-03
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createDailyByDay(String day) { // 2019-01-19 日期

        //1、先根据这个时间来查询是否统计过
        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        Daily daily = baseMapper.selectOne(wrapper);
        //2、如果统计过、先删除
        if(daily != null){
            baseMapper.deleteById(daily.getId());
        }
        //3、覆盖行吗?可以！！！
        /*if(daily != null){
            //修改里面统计的数据
            daily.setRegisterNum();
            daily.setLoginNum();
            daily.setVideoViewNum();
            daily.setCourseNum();
            daily.setGmtModified(new Date());
            baseMapper.updateById(daily);
        }*/
        //3、添加
        daily = new Daily();
        //添加需要统计的属性:这些属性应该是远程调用过来的！
        Integer registerNum = (Integer) ucenterClient.getRegsiterNumByDay(day).getData().get("count");
        //登陆人数
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        //视频播放数
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        //课程数
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        /*daily.setGmtCreate(new Date());
        daily.setGmtModified(new Date());*/
        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> showCharts(String type, String begin, String end) {

        Map<String, Object> map = new HashMap<>();

        QueryWrapper<Daily> wrapper = new QueryWrapper<>();
        //显示什么字段名
        wrapper.select("date_calculated", type);
        wrapper.between("date_calculated", begin , end);
        wrapper.orderByAsc("date_calculated"); //升序
        //根据上面条件查出对应的Daily集合
        List<Daily> dailyList = baseMapper.selectList(wrapper);

        List<Integer> typeList = new ArrayList<>(); //查询类型数据集合
        List<String> dateList = new ArrayList<>(); //时间集合
        //遍历此对象把数据放在两个集合中
        for(Daily daily : dailyList){
            dateList.add(daily.getDateCalculated());
            //统计的什么类型的数据放在集合中
            switch (type){
                case "register_num":
                    typeList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    typeList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    typeList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    typeList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        map.put("typeList", typeList);
        map.put("dateList", dateList);
        return map;
    }
}
