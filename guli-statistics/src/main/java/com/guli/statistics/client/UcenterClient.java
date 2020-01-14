package com.guli.statistics.client;

import com.guli.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Component //需要被加载到容器中
@FeignClient("guli-ucenter") //声明此接口调用哪个服务
public interface UcenterClient {

    /**
     * 此接口调用服务中的什么接口
     * @param day
     * @return
     * 注意： 在被调用的服务接口中 @PathVariable 一定要加
     */
    @GetMapping("/ucenter/member/regsiterNum/{day}")
    public Result getRegsiterNumByDay(@PathVariable("day") String day);
}
