package com.aomsir.test;

import com.aomsir.autoconfig.template.AipFaceTemplate;
import com.aomsir.server.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@SpringBootTest(classes = AppServerApplication.class)
@RunWith(SpringRunner.class)
public class FaceTest {
    @Resource
    private AipFaceTemplate template;

    @Test
    public void testderect() {
        boolean detect = template.detect("xxxxx4xxxxxx6-xxxx87-21149adac2e6.jpg");
        System.out.println(detect);
    }


//    //设置APPID/AK/SK
//    public static final String APP_ID = "xxxx";
//    public static final String API_KEY = "xxxx";
//    public static final String SECRET_KEY = "xxxx";
//
//    public static void main(String[] args) {
//        // 初始化一个AipFace
//        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
//
//        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);
//
//        // 调用接口
//        String image = "xxxxx";
//        String imageType = "URL";
//
//        // 传入可选参数调用接口
//        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("face_field", "age");
//        options.put("max_face_num", "2");
//        options.put("face_type", "LIVE");
//        options.put("liveness_control", "LOW");
//
//        // 人脸检测
//        JSONObject res = client.detect(image, imageType, options);
//        System.out.println(res.toString(2));
//
//    }
}
