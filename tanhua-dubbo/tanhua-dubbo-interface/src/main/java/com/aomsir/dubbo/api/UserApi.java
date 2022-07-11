package com.aomsir.dubbo.api;

import com.aomsir.model.domain.User;

public interface UserApi {

    // 根据手机号查询用户
    User findByMobile(String mobile);


    // 保存用户,返回用户ID
    Long save(User user);
}
