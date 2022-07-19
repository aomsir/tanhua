package com.aomsir.server.service;

import com.aomsir.autoconfig.template.OssTemplate;
import com.aomsir.dubbo.api.MovementApi;
import com.aomsir.model.mongo.Movement;
import com.aomsir.model.vo.ErrorResult;
import com.aomsir.server.exception.BusinessException;
import com.aomsir.server.interceptor.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
public class MovementService {
    @Autowired
    private OssTemplate ossTemplate;

    @DubboReference
    private MovementApi movementApi;

    /**
     * 发布动态
     * @param movement
     * @param imageContent
     */
    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        // 1.判断发布的动态内容是否存在
        if (StringUtils.isEmpty(movement.getTextContent())) {
            throw new BusinessException(ErrorResult.contentError());
        }

        // 2.获取当前登录的用户ID
        Long userId = UserHolder.getUserId();

        // 3.将文件上传到阿里云OSS,获取请求地址
        List<String> medias = new ArrayList<>();
        for (MultipartFile multipartFile : imageContent) {
            String update = ossTemplate.update(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            medias.add(update);
        }

        // 4.将数据封装到Movement
        movement.setUserId(userId);
        movement.setMedias(medias);

        // 5.调用API完成动态发布
        movementApi.publish(movement);

    }
}
