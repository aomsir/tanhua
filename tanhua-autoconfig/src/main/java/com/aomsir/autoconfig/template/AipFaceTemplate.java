package com.aomsir.autoconfig.template;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class AipFaceTemplate {
    @Autowired
    private AipFace client;

    /**
     * 检测人脸中是否包含人脸
     * true: 包含
     * false: 不包含
     * @return
     */
    public boolean detect(String imageUrl) {
        String imageType = "URL";

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");

        // 人脸检测
        JSONObject res = client.detect(imageUrl, imageType, options);
        Integer error_code = (Integer) res.get("error_code");


        return error_code == 0;
    }
}
