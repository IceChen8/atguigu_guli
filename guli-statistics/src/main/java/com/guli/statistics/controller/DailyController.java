package com.guli.statistics.controller;

import com.guli.common.vo.Result;
import com.guli.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author huaan
 * @since 2020-01-03
 */
@RestController
@CrossOrigin
@RequestMapping("/statistics/daily")
@Api(description = "统计分析")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation(value = "根据时间生成统计")
    @GetMapping("createDaily/{day}")
    public Result createDailyByDay(
            @ApiParam(name = "day" , value = "时间")
            @PathVariable("day") String day){

        dailyService.createDailyByDay(day);
        return Result.ok();
    }

    @ApiOperation(value = "根据查询类型及时间查统计报表")
    @GetMapping("showCharts")
    public Result showCharts(
            @ApiParam(name = "type",value = "查询类型")
            @RequestParam String type,
            @ApiParam(name = "begin",value = "开始时间")
            @RequestParam String begin,
            @ApiParam(name = "end",value = "结束时间")
            @RequestParam String end){

        Map<String, Object> map = dailyService.showCharts(type, begin, end);
        return Result.ok().data(map);
    }

}

