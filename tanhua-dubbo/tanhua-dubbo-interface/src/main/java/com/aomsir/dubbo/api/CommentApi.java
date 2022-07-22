package com.aomsir.dubbo.api;

import com.aomsir.model.enums.CommentType;
import com.aomsir.model.mongo.Comment;

import java.util.List;

public interface CommentApi {
    // 发布评论并获取评论数量
    Integer save(Comment comment1);

    // 分页实现评论列表
    List<Comment> findComments(String movementId, CommentType comment, Integer page, Integer pageSize);

    Boolean hasComment(String movementId, Long userId, CommentType like);

    Integer remove(Comment comment);
}
