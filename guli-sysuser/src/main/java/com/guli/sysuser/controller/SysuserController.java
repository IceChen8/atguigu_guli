package com.guli.sysuser.controller;

import com.guli.common.vo.Result;
import com.guli.sysuser.entity.Sysuser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
@CrossOrigin
@Api(description="系统用户管理")
public class SysuserController {

    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public Result login(
            @ApiParam(name = "sysuser",value = "系统用户对象",required = true)
            @RequestBody Sysuser sysuser){

        return Result.ok().data("token","admin");
    }

    @GetMapping("info")
    @ApiOperation(value = "获取用户信息")
    public Result info(
            @ApiParam(name = "token", value = "令牌", required = true)
            @RequestParam String token){
        return Result.ok()
                .data("roles", "admin")
                .data("name", "admin")
                .data("avatar","https://guli0722ice.oss-cn-shenzhen.aliyuncs.com/avatar/2019/12/30/22c732f8-f774-4e14-bc36-7216e07ddf12.jpg");
    }

    @PostMapping("logout")
    @ApiOperation(value = "用户登出")
    public Result logout(){
        return Result.ok();
    }
}
