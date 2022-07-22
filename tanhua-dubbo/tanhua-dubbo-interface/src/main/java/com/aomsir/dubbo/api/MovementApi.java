package com.aomsir.dubbo.api;

import com.aomsir.model.mongo.Movement;
import com.aomsir.model.vo.PageResult;

import java.util.List;

public interface MovementApi {
    void publish(Movement movement);

    PageResult findByUserId(Long userId, Integer page, Integer pagesize);

    List<Movement> findFriendMovements(Integer page, Integer pagesize, Long userId);

    List<Movement> randomMovements(Integer pagesize);

    List<Movement> findMovementsByPids(List<Long> pids);

    Movement findById(String movementId);
}
