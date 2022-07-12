package com.aomsir.server.service;

import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
public class UserInfoService {
    @DubboReference
    private UserInfoApi userInfoApi;

    public void save(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }
}
