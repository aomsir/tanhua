package com.aomsir.dubbo.api;

import com.aomsir.model.enums.CommentType;
import com.aomsir.model.mongo.Comment;
import com.aomsir.model.mongo.Movement;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/7/22
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@DubboService
public class CommentApiImpl implements CommentApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Integer save(Comment comment) {
        // 1.查询动态
        Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);

        // 2.向comment对象设置被评论人属性
        if (movement != null) {
            comment.setPublishUserId(movement.getUserId());
        }

        // 3.保存到数据库
        mongoTemplate.save(comment);

        // 4.更新动态表中的字段
        Query query = Query.query(Criteria.where("id").is(comment.getPublishId()));

        Update update = new Update();
        if (comment.getCommentType() == CommentType.LIKE.getType()) {
            update.inc("likeCount",1);
        } else if (comment.getCommentType() == CommentType.COMMENT.getType()) {
            update.inc("commentCount",1);
        } else {
            update.inc("loveCount",1);
        }

        // 设置更新参数
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        Movement modify = mongoTemplate.findAndModify(query, update, options, Movement.class);

        // 5.获取最新评论数量并返回
        return modify.statisCount(comment.getCommentType());
    }


    // 评论列表
    @Override
    public List<Comment> findComments(String movementId, CommentType commentType, Integer page, Integer pageSize) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(movementId))
                .and("commentType").is(commentType.getType()))
                .skip((page - 1)*pageSize)
                .limit(pageSize)
                .with(Sort.by(Sort.Order.desc("created")));
        return mongoTemplate.find(query,Comment.class);
    }

    // 动态点赞
    @Override
    public Boolean hasComment(String movementId, Long userId, CommentType commentType) {
        Criteria criteria = Criteria.where("userId").is(userId)
                .and("publishId").is(new ObjectId(movementId))
                .and("commentType").is(commentType.getType());

        Query query = Query.query(criteria);
        return mongoTemplate.exists(query,Comment.class);
    }


    // 动态点赞取消
    @Override
    public Integer remove(Comment comment) {
        // 1.删除Comment表数据
        Criteria criteria = Criteria.where("userId").is(comment.getUserId())
                .and("publishId").is(comment.getPublishId())
                .and("commentType").is(comment.getCommentType());
        Query query = Query.query(criteria);
        mongoTemplate.remove(query,Comment.class);

        // 2.修改动态表中的总数据
        Query movementQuery = Query.query(Criteria.where("id").is(comment.getPublishId()));

        Update update = new Update();
        if (comment.getCommentType() == CommentType.LIKE.getType()) {
            update.inc("likeCount",-1);
        } else if (comment.getCommentType() == CommentType.COMMENT.getType()) {
            update.inc("commentCount",-1);
        } else {
            update.inc("loveCount",-1);
        }

        // 设置更新参数
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        Movement modify = mongoTemplate.findAndModify(movementQuery, update, options, Movement.class);

        // 5.获取最新评论数量并返回
        return modify.statisCount(comment.getCommentType());
    }
}
