package com.aomsir.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aomsir.autoconfig.template.OssTemplate;
import com.aomsir.server.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class OssTest {

    @Resource
    private OssTemplate ossTemplate;


    @Test
    public void testTemplateUpdate() throws Exception{
        String path = "/Users/aomsir/Pictures/背景/壁纸/38.JPG";
        FileInputStream fis = new FileInputStream(new File(path));
        String url = ossTemplate.update("1.jpg", fis);
        System.out.println(url);
    }


    @Test
    public void testOssUpdate() throws Exception{
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "xxxx";
        String accessKeyId = "xxxx";
        String accessKeySecret = "xxxx";
        String bucketName = "xxxx";


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String path = "/Users/aomsir/Downloads/iShot_2022-07-11_21.21.38.png";
        FileInputStream fis = new FileInputStream(new File(path));
        String fileName = new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                +"/"+ UUID.randomUUID().toString() + path.substring(path.lastIndexOf("."));


        // 创建PutObject请求。
        ossClient.putObject(bucketName, fileName, fis);

        ossClient.shutdown();
    }
}
