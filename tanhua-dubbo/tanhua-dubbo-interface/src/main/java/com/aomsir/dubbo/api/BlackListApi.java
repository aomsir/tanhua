package com.aomsir.dubbo.api;

import com.aomsir.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BlackListApi {
    // 分页查询黑名单列表
    IPage<UserInfo> findByUserId(Long userId, int page, int size);
}
