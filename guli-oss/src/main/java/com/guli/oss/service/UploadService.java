package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * oss文件上传
     * @param file
     * @return
     */
    String updoad(MultipartFile file,String host);
}
