package com.guli.ucenter.controller;


import com.guli.common.vo.Result;
import com.guli.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2020-01-03
 */
@CrossOrigin
@RestController
@RequestMapping("/ucenter/member")
@Api(description = "用户中心")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "获取今日注册数")
    @GetMapping("regsiterNum/{day}")
    public Result getRegsiterNumByDay(
            @ApiParam(name = "day" , value = "统计日期")
            @PathVariable("day") String day){

        Integer count = memberService.getRegsiterNumByDay(day);

        return Result.ok().data("count",count);
    }

}

