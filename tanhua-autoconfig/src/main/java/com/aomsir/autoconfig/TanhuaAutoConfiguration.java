package com.aomsir.autoconfig;

import com.aomsir.autoconfig.properties.AipFaceProperties;
import com.aomsir.autoconfig.properties.OssProperties;
import com.aomsir.autoconfig.properties.SmsProperties;
import com.aomsir.autoconfig.template.AipFaceTemplate;
import com.aomsir.autoconfig.template.OssTemplate;
import com.aomsir.autoconfig.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@EnableConfigurationProperties(
        {SmsProperties.class,
        OssProperties.class, AipFaceProperties.class}
)
public class TanhuaAutoConfiguration {
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        return new SmsTemplate(smsProperties);
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        return new OssTemplate(ossProperties);
    }

    @Bean
    public AipFaceTemplate aipFaceTemplate() {
        return new AipFaceTemplate();
    }
}
