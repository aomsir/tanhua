package com.aomsir.dubbo.api;

import com.aomsir.dubbo.utils.IdWorker;
import com.aomsir.model.mongo.Friend;
import com.aomsir.model.mongo.Movement;
import com.aomsir.model.mongo.MovementTimeLine;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@DubboService
public class MovementApiImpl implements MovementApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    // 发布动态
    @Override
    public void publish(Movement movement) {
        // 1.设置PID和时间,保存用户动态详情
        movement.setPid(idWorker.getNextId("movement"));
        movement.setCreated(System.currentTimeMillis());
        mongoTemplate.save(movement);

        // 2.查询当前用户的好友数据
        Criteria criteria = Criteria.where("userId").is(movement.getUserId());
        Query query = Query.query(criteria);
        List<Friend> friends = mongoTemplate.find(query, Friend.class);

        // 3.循环好友数据,构建时间线数据存入数据库
        for (Friend friend : friends) {
            MovementTimeLine timeLine = new MovementTimeLine();
            timeLine.setMovementId(movement.getId());
            timeLine.setUserId(friend.getUserId());
            timeLine.setFriendId(friend.getFriendId());
            timeLine.setCreated(System.currentTimeMillis());
            mongoTemplate.save(timeLine);
        }
    }
}
