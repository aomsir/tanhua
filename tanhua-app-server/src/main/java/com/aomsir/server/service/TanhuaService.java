package com.aomsir.server.service;

import com.aomsir.dubbo.api.RecommendUserApi;
import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.mongo.RecommendUser;
import com.aomsir.model.vo.TodayBest;
import com.aomsir.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@Service
public class TanhuaService {

    @DubboReference
    private RecommendUserApi recommendUserApi;

    @DubboReference
    private UserInfoApi userInfoApi;

    public TodayBest todayBest() {
        // 1.获取用户ID
        Long userId = UserHolder.getUserId();

        // 2.调用API查询
        RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(userId);
        if (recommendUser == null) {
            recommendUser = new RecommendUser();
            recommendUser.setToUserId(123l);
            recommendUser.setScore(99d);

        }

        // 3.将RecommendUser转化为VO对象
        UserInfo userInfo = userInfoApi.findById(recommendUser.getToUserId());

        // 调用封装的复制方法
        TodayBest vo = TodayBest.init(userInfo, recommendUser);

        // 4.返回
        return vo;


    }
}
