package com.aomsir.autoconfig.template;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aomsir.autoconfig.properties.OssProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

public class OssTemplate {
    private final OssProperties properties;

    public OssTemplate(OssProperties properties) {
        this.properties = properties;
    }

    /**
     * 文件上传
     * @param fileName
     * @param is
     * @return
     */
    public String update(String fileName, InputStream is) {
        // 配置OSS
        String endpoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKey();
        String accessKeySecret = properties.getSecret();
        String bucketName = properties.getBucketName();


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        // 拼写路径，保证唯一性
        fileName = new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                +"/"+ UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));


        // 创建PutObject请求。
        ossClient.putObject(bucketName, fileName, is);

        // 关闭OSSClient
        ossClient.shutdown();

        String url = properties.getUrl() + "/" + fileName;
        return url;
    }
}
