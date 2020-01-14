package com.guli.statistics.service;

import com.guli.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author huaan
 * @since 2020-01-03
 */
public interface DailyService extends IService<Daily> {

    /**
     * 根据时间生成统计
     * @param day
     */
    void createDailyByDay(String day);

    /**
     * 根据统计类型和时间查询统计数据
     * @param type
     * @param begin
     * @param end
     * @return
     */
    Map<String,Object> showCharts(String type, String begin, String end);
}
