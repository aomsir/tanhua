package com.aomsir.autoconfig.properties;

import com.baidu.aip.face.AipFace;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Data
@ConfigurationProperties(prefix = "tanhua.aip")
public class AipFaceProperties {
    private String appId;
    private String apiKey;
    private String secretKey;

    /**
     * 实现单例注入
     * @return
     */
    @Bean
    public AipFace aipFace() {
        // 初始化一个AipFace
        AipFace client = new AipFace(appId, apiKey, secretKey);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        return client;
    }
}
