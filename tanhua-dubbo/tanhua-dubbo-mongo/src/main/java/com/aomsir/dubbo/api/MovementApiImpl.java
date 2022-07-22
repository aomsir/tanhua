package com.aomsir.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.aomsir.dubbo.utils.IdWorker;
import com.aomsir.dubbo.utils.TimeLineService;
import com.aomsir.model.mongo.Movement;
import com.aomsir.model.mongo.MovementTimeLine;
import com.aomsir.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
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

    @Autowired
    private TimeLineService timeLineService;

    // 发布动态
    @Override
    public void publish(Movement movement) {
        // 1.设置PID和时间,保存用户动态详情
        movement.setPid(idWorker.getNextId("movement"));
        movement.setCreated(System.currentTimeMillis());
        mongoTemplate.save(movement);

        // 代码抽取,使其支持异步调用
        timeLineService.saveTimeLine(movement.getUserId(),movement.getId());
    }

    // 获取我的动态详情
    @Override
    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {
        Criteria criteria = Criteria.where("userId").is(userId);
        Query query = Query.query(criteria).
                skip((page - 1)*pagesize).
                limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));

        List<Movement> movements = mongoTemplate.find(query, Movement.class);

        return new PageResult(page,pagesize,0l,movements);
    }


    @Override
    public List<Movement> findFriendMovements(Integer page, Integer pagesize, Long friendId) {
        // 1.根据friendId查询时间线表
        Query query = Query.query(Criteria.where("friendId").is(friendId))
                .skip((page - 1)*pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        List<MovementTimeLine> movementTimeLines = mongoTemplate.find(query, MovementTimeLine.class);

        // 2.提取动态id列表
        List<ObjectId> movementId = CollUtil.getFieldValues(movementTimeLines, "movementId", ObjectId.class);

        // 3.根据动态id查询动态详情
        Query movementQuery = Query.query(Criteria.where("id").in(movementId));
        List<Movement> list = mongoTemplate.find(movementQuery, Movement.class);

        return list;
    }


    @Override
    public List<Movement> randomMovements(Integer pagesize) {
        // 1.创建统计对象,设置统计参数
        TypedAggregation aggregation = TypedAggregation.newAggregation(Movement.class, Aggregation.sample(pagesize));

        // 2.调用方法统计
        AggregationResults<Movement> result = mongoTemplate.aggregate(aggregation, Movement.class);

        // 3.获取统计结果
        return result.getMappedResults();
    }

    @Override
    public List<Movement> findMovementsByPids(List<Long> pids) {
        Query query = Query.query(Criteria.where("pids").in(pids));
        return mongoTemplate.find(query, Movement.class);
    }

    @Override
    public Movement findById(String movementId) {

        return mongoTemplate.findById(movementId,Movement.class);
    }
}
