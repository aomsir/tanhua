package com.aomsir.dubbo.api;

import com.aomsir.model.mongo.RecommendUser;
import com.aomsir.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@DubboService
public class RecommendUserApiImpl implements RecommendUserApi{

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 查询今日佳人
     * @param toUserId
     * @return
     */
    @Override
    public RecommendUser queryWithMaxScore(Long toUserId) {
        //根据toUserId查询，根据评分score排序，获取第一条
        //构建Criteria
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        //构建Query对象
        Query query = Query.query(criteria)
                .with(Sort.by(Sort.Order.desc("score")))
                .limit(1);
        //调用mongoTemplate查询

        return mongoTemplate.findOne(query,RecommendUser.class);
    }


    /**
     * 分页查询首页推荐列表分页数据
     * @param page
     * @param pagesize
     * @param toUserId
     * @return
     */
    @Override
    public PageResult queryRecommendUserList(Integer page, Integer pagesize, Long toUserId) {
        // 1.构建Criteria对象
        Criteria criteria = Criteria.where("toUserId").is(toUserId);

        // 2.构建query对象
        Query query = Query.query(criteria);

        // 3.查询总数
        long count = mongoTemplate.count(query, RecommendUser.class);

        // 4.查询数据列表
        query.with(Sort.by(Sort.Order.desc("score")))
                .limit((page - 1) * pagesize)
                .skip(pagesize);
        List<RecommendUser> list = mongoTemplate.find(query, RecommendUser.class);

        // 5.构造返回值
        return new PageResult(page,pagesize,count,list);
    }
}
