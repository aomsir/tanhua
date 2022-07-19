package com.aomsir.dubbo.api;

import com.aomsir.model.mongo.Movement;

public interface MovementApi {
    void publish(Movement movement);
}
