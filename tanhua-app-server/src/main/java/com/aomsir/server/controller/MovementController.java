package com.aomsir.server.controller;

import com.aomsir.model.mongo.Movement;
import com.aomsir.server.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@RestController
@RequestMapping("/movement")
public class MovementController {
    @Autowired
    private MovementService movementService;

    @PostMapping
    public ResponseEntity<?> movements(Movement movement,
                                       MultipartFile imageContent[]) throws Exception{

        movementService.publishMovement(movement,imageContent);
        return ResponseEntity.ok().body(null);
    }

}
