package com.aomsir.dubbo.api;

import com.aomsir.model.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoApi {
    void save(UserInfo userInfo);

    void update(UserInfo userInfo);

    UserInfo findById(Long id);

    /**
     * 批量查询用户详情
     * @param userIds
     * @param info
     * @return
     */
    Map<Long,UserInfo> findByIds(List<Long> userIds,UserInfo info);
}
