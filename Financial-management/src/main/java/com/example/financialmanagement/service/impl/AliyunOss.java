package com.example.financialmanagement.service.impl;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-07-03 13:48
 */

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Service
public class AliyunOss {

  public static String uploadFile(MultipartFile multipartFile){
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    String endpoint = "oss-cn-shanghai.aliyuncs.com";
    // accessKeyId 和 accessKeySecret 是先前创建用户生成的
    String accessKeyId = "";
    String accessKeySecret = "";
    // 填写Bucket名称，例如examplebucket。
    String bucketName = "neulogistics";
    // 创建OSSClient实例。
    OSS ossClient = null;

    try {
      ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
      //如果同不存在，创建桶
      if (! ossClient.doesBucketExist(bucketName)){
        //创建bucket
        ossClient.createBucket(bucketName);
        //设置oss实例的访问权限：公共
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
      }
      //2.获取文件上传流
      InputStream inputStream = multipartFile.getInputStream();
      //3.构建日期目录
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      String datePath = dateFormat.format(new Date());
      //4.获取文件名
      String originname = multipartFile.getOriginalFilename();
      //5.文件上传阿里云服务器
      ossClient.putObject(bucketName, originname, inputStream);
      return "https://"+bucketName+"."+endpoint+"/"+originname;
    } catch (Exception e) {
      e.printStackTrace();
      return "oss fail";
    } finally {
      if (ossClient != null) {
        ossClient.shutdown();
      }
    }
  }
}
