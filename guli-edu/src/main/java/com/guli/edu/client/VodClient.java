package com.guli.edu.client;

import com.guli.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("guli-vod")
public interface VodClient {
    @DeleteMapping("vod/video/removeByVideoSourceId/{videoSourceId}")
    public Result removeByVideoSourceId(@PathVariable("videoSourceId") String videoSourceId);

}
