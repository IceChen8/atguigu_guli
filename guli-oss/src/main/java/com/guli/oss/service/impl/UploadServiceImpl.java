package com.guli.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.UploadService;
import com.guli.oss.utill.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String updoad(MultipartFile file,String host) {

        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        //String fileHost = ConstantPropertiesUtil.FILE_HOST;

        List<String> fileTypes = ConstantPropertiesUtil.FILE_TYPES;

        String url = "";

        Boolean flag = false;
        try {
            //获取照片名
            String originalFilename = file.getOriginalFilename();


            for (String type : fileTypes) {
                if (StringUtils.endsWithIgnoreCase(originalFilename,type)){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                throw new GuliException(ResultCodeEnum.FILE_UPLOADTYPE_ERROR);
            }

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null){
                throw new GuliException(ResultCodeEnum.FILE_UPLOADCONTEXT_ERROR);
            }else{
                System.out.println("文件的宽度："+image.getWidth());
                System.out.println("文件的高度："+image.getHeight());
            }

            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            /*if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }*/

            //获取上传文件流
            InputStream inputStream = file.getInputStream();
            //构建日期路径：avatar/2019/02/26/文件名
            String filePath = new DateTime().toString("yyy/MM/dd");//用到了日期工具类，已经导了依赖
            

            //文件名：uuid.扩展名
            //获取一个上传文件的随机名 因为如果用原来上传的文件名的话有可能上传相同的文件名
            String fileName = UUID.randomUUID().toString();
         
            //获取照片后缀名
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            //新的照片文件名
            String newName = fileName+fileType;
            //构建 avatar/2019/12/30/22c732f8-f774-4e14-bc36-7216e07ddf12.jpg
            String fileUrl = host + "/" +  filePath + "/" + newName;

            //文件上传至阿里云
            ossClient.putObject(bucketName, fileUrl, inputStream);

            //拼出url地址
            url = "http://" + bucketName + "." + endPoint + "/" + fileUrl;

            //不管上传成不成功都要关闭
            ossClient.shutdown();
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }

        return url;
    }
}
