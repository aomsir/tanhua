package com.aomsir.server.service;

import com.aomsir.autoconfig.template.AipFaceTemplate;
import com.aomsir.autoconfig.template.OssTemplate;
import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.vo.ErrorResult;
import com.aomsir.model.vo.UserInfoVo;
import com.aomsir.server.exception.BusinessException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub:  https://github.com/aomsir
 */

@Service
public class UserInfoService {
    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    AipFaceTemplate aipFaceTemplate;

    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }

    // 更新用户头像
    public void updateHead(MultipartFile headPhoto, Long id) throws IOException {
        // 1.将头像上传到阿里云OSS
        String imageUrl = ossTemplate.update(headPhoto.getOriginalFilename(), headPhoto.getInputStream());

        // 2.调用百度人脸识别服务检测是否包含人脸
        boolean detect = aipFaceTemplate.detect(imageUrl);

        // 3.检测人脸
        if (!detect) {
            // 不包含人脸，抛出异常
            throw new BusinessException(ErrorResult.faceError());
        } else {
            // 包含人脸，调用API及时更新
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setAvatar(imageUrl);

            // 这次更新会根据id更新
            userInfoApi.update(userInfo);
        }
    }

    // 根据id查询
    public UserInfoVo findById(Long id) {
        UserInfo userInfo = userInfoApi.findById(id);

        UserInfoVo vo = new UserInfoVo();

        // copy类型一致的属性
        BeanUtils.copyProperties(userInfo,vo);
        if (userInfo.getAge() != null) {
            vo.setAge(userInfo.getAge().toString());
        }

        return vo;
    }

    // 更新用户信息
    public void update(UserInfo userInfo) {
        userInfoApi.update(userInfo);
    }
}
