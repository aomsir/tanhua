package com.aomsir.dubbo.api;

import com.aomsir.model.mongo.RecommendUser;

public interface RecommendUserApi {
    RecommendUser queryWithMaxScore(Long toUserId);
}
