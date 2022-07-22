package com.aomsir.server.service;

import cn.hutool.core.collection.CollUtil;
import com.aomsir.commons.utils.Constants;
import com.aomsir.dubbo.api.CommentApi;
import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.enums.CommentType;
import com.aomsir.model.mongo.Comment;
import com.aomsir.model.vo.CommentVo;
import com.aomsir.model.vo.ErrorResult;
import com.aomsir.model.vo.PageResult;
import com.aomsir.server.exception.BusinessException;
import com.aomsir.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/22
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
@Slf4j
public class CommentsService {

    @DubboReference
    private CommentApi commentApi;

    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 发布评论
    public void saveComments(String movementId, String comment) {
        // 1.获取操作用户ID
        Long userId = UserHolder.getUserId();

        // 2.构造Comment
        Comment comment1 = new Comment();
        comment1.setPublishId(new ObjectId(movementId));
        comment1.setCommentType(CommentType.COMMENT.getType());
        comment1.setContent(comment);
        comment1.setUserId(userId);
        comment1.setCreated(System.currentTimeMillis());

        // 3.调用API保存数据
        Integer commentCount = commentApi.save(comment1);

        log.info("commentCount = " + commentCount);
    }

    // 分页查询评论列表
    public PageResult findComments(String movementId, Integer page, Integer pageSize) {
        // 1.调用API查询评论列表
        List<Comment> list = commentApi.findComments(movementId,CommentType.COMMENT,page,pageSize);

        // 2.判断list集合是否存在
        if (CollUtil.isEmpty(list)) {
            return new PageResult();
        }

        // 3.提取所有用户的id,调用userInfoService查询用户详情
        List<Long> userIds = CollUtil.getFieldValues(list, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(userIds, null);

        // 4.构造VO对象
        List<CommentVo> vos = new ArrayList<>();
        for (Comment comment : list) {
            UserInfo userInfo = infoMap.get(comment.getUserId());
            if (userInfo != null) {
                CommentVo vo = CommentVo.init(userInfo,comment);
                vos.add(vo);
            }
        }

        // 4.构造返回值
        return new PageResult(page,pageSize,0l,vos);
    }

    // 点赞动态
    public Integer likeComment(String movementId) {
        // 1.调用API查询是否已点赞
        Boolean hasComment = commentApi.hasComment(movementId,UserHolder.getUserId(),CommentType.LIKE);

        // 2.已点赞,抛出异常
        if (hasComment) {
            throw new BusinessException(ErrorResult.likeError());
        }

        // 3.调用API保存数据到mongodb
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setCommentType(CommentType.LIKE.getType());
        comment.setUserId(UserHolder.getUserId());
        comment.setCreated(System.currentTimeMillis());
        Integer likeCount = commentApi.save(comment);

        // 4.拼接redis的key,将用户的点赞状态存入Redis
        String key = Constants.MOVEMENTS_INTERACT_KEY + movementId;
        String hashKey = Constants.MOVEMENT_LIKE_HASHKEY +UserHolder.getUserId();
        redisTemplate.opsForHash().put(key,hashKey,"1");

        return likeCount;
    }

    //取消点赞
    public Integer dislikeComment(String movementId) {
        // 1.调用API查询是否已喜欢
        Boolean hasComment = commentApi.hasComment(movementId,UserHolder.getUserId(),CommentType.LIKE);

        // 2.如果未喜欢,则抛出异常
        if (hasComment) {
            throw new BusinessException(ErrorResult.disLikeError());
        }

        // 3.调用API删除数据,返回喜欢数据
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setCommentType(CommentType.LIKE.getType());
        comment.setUserId(UserHolder.getUserId());
        Integer likeCount = commentApi.remove(comment);

        // 4.拼接redis的Key,删除喜欢状态
        String key = Constants.MOVEMENTS_INTERACT_KEY + movementId;
        String hashKey = Constants.MOVEMENT_LIKE_HASHKEY +UserHolder.getUserId();
        redisTemplate.opsForHash().delete(key,hashKey);

        return likeCount;
    }

    // 喜欢动态
    public Integer loveComment(String movementId) {
        // 1.调用API查询是否已点赞
        Boolean hasComment = commentApi.hasComment(movementId,UserHolder.getUserId(),CommentType.LOVE);

        // 2.已点赞,抛出异常
        if (hasComment) {
            throw new BusinessException(ErrorResult.loveError());
        }

        // 3.调用API保存数据到mongodb
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setCommentType(CommentType.LOVE.getType());
        comment.setUserId(UserHolder.getUserId());
        comment.setCreated(System.currentTimeMillis());
        Integer loveCount = commentApi.save(comment);

        // 4.拼接redis的key,将用户的点赞状态存入Redis
        String key = Constants.MOVEMENTS_INTERACT_KEY + movementId;
        String hashKey = Constants.MOVEMENT_LOVE_HASHKEY +UserHolder.getUserId();
        redisTemplate.opsForHash().put(key,hashKey,"1");

        return loveCount;
    }

    // 取消喜欢动态
    public Integer disloveComment(String movementId) {
        // 1.调用API查询是否已喜欢
        Boolean hasComment = commentApi.hasComment(movementId,UserHolder.getUserId(),CommentType.LOVE);

        // 2.如果未喜欢,则抛出异常
        if (hasComment) {
            throw new BusinessException(ErrorResult.disloveError());
        }

        // 3.调用API删除数据,返回喜欢数据
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setCommentType(CommentType.LOVE.getType());
        comment.setUserId(UserHolder.getUserId());
        Integer loveCount = commentApi.remove(comment);

        // 4.拼接redis的Key,删除喜欢状态
        String key = Constants.MOVEMENTS_INTERACT_KEY + movementId;
        String hashKey = Constants.MOVEMENT_LOVE_HASHKEY +UserHolder.getUserId();
        redisTemplate.opsForHash().delete(key,hashKey);

        return loveCount;
    }
}
