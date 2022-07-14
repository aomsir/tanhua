package com.aomsir.dubbo.api;

import com.aomsir.model.mongo.RecommendUser;
import com.aomsir.model.vo.PageResult;

public interface RecommendUserApi {
    // 今日佳人
    RecommendUser queryWithMaxScore(Long toUserId);

    // 分页查询
    PageResult queryRecommendUserList(Integer page, Integer pagesize, Long toUserId);
}
