package com.aomsir.server.service;

import cn.hutool.core.collection.CollUtil;
import com.aomsir.dubbo.api.RecommendUserApi;
import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.dto.RecommendUserDto;
import com.aomsir.model.mongo.RecommendUser;
import com.aomsir.model.vo.PageResult;
import com.aomsir.model.vo.TodayBest;
import com.aomsir.server.interceptor.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            recommendUser.setToUserId(1l);
            recommendUser.setScore(99d);

        }

        // 3.将RecommendUser转化为VO对象
        UserInfo userInfo = userInfoApi.findById(recommendUser.getToUserId());

        // 调用封装的复制方法
        TodayBest vo = TodayBest.init(userInfo, recommendUser);

        // 4.返回
        return vo;
    }

//    /**
//     * 查询分页推荐好友列表
//     * @param dto
//     * @return
//     */
//    public PageResult recommendation(RecommendUserDto dto) {
//        //1、获取用户id
//        Long userId = UserHolder.getUserId();
//        //2、调用recommendUserApi分页查询数据列表（PageResult -- RecommendUser）
//        PageResult pr = recommendUserApi.queryRecommendUserList(dto.getPage(),dto.getPagesize(),userId);
//        //3、获取分页中的RecommendUser数据列表
//        List<RecommendUser> items = (List<RecommendUser>) pr.getItems();
//        //4、判断列表是否为空
//        if(items == null) {
//            return pr;
//        }
//        //5、循环RecommendUser数据列表，根据推荐的用户id查询用户详情
//        List<TodayBest> list = new ArrayList<>();
//        for (RecommendUser item : items) {
//            Long recommendUserId = item.getUserId();
//            UserInfo userInfo = userInfoApi.findById(recommendUserId);
//            if(userInfo != null) {
//                //条件判断
//                if(!StringUtils.isEmpty(dto.getGender()) && !dto.getGender().equals(userInfo.getGender())) {
//                    continue;
//                }
//                if(dto.getAge() != null && dto.getAge() < userInfo.getAge()) {
//                    continue;
//                }
//                TodayBest vo = TodayBest.init(userInfo, item);
//                list.add(vo);
//            }
//        }
//        //6、构造返回值
//        pr.setItems(list);
//        return pr;
//    }

    /**
     * 查询分页推荐好友列表优化版本
     * 不会频繁调用远程数据库
     * @param dto
     * @return
     */
    public PageResult recommendation(RecommendUserDto dto) {
        // 1、获取用户id
        Long userId = UserHolder.getUserId();

        // 2、调用recommendUserApi分页查询数据列表（PageResult -- RecommendUser）
        PageResult pr = recommendUserApi.queryRecommendUserList(dto.getPage(),dto.getPagesize(),userId);

        // 3、获取分页中的RecommendUser数据列表
        List<RecommendUser> items = (List<RecommendUser>) pr.getItems();

        // 4、判断列表是否为空
        if(items == null) {
            return pr;
        }

        // 5.提取所有用户的id列表
        List<Long> ids = CollUtil.getFieldValues(items, "userId",Long.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(dto.getAge());
        userInfo.setGender(dto.getGender());

        // 6.构建查询条件，批量查询所有的用户详情
        Map<Long, UserInfo> map = userInfoApi.findByIds(ids, userInfo);

        // 7.循环推荐的数据列表,构建vo对象
        List<TodayBest> list = new ArrayList<>();
        for (RecommendUser item : items) {
            UserInfo info = map.get(item.getUserId());
            if (info != null) {
                TodayBest vo = TodayBest.init(userInfo, item);
                list.add(vo);
            }

        }

        // 8.构造返回
        pr.setItems(list);
        return pr;
    }
}
