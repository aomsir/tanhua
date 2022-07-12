package com.aomsir.dubbo.api;

import com.aomsir.model.domain.UserInfo;

public interface UserInfoApi {
    public void save(UserInfo userInfo);

    public void update(UserInfo userInfo);

    UserInfo findById(Long id);
}
