package com.aomsir.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Data
@ConfigurationProperties(prefix = "tanhua.oss")
public class OssProperties {
    private String accessKey;
    private String secret;
    private String bucketName;
    private String url;
    private String endpoint;
}
