package com.aomsir.server.controller;

import com.aomsir.model.mongo.Movement;
import com.aomsir.model.vo.MovementsVo;
import com.aomsir.model.vo.PageResult;
import com.aomsir.server.service.CommentsService;
import com.aomsir.server.service.MovementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Slf4j
@RestController
@RequestMapping("/movements")
public class MovementController {
    @Autowired
    private MovementService movementService;

    @Autowired
    private CommentsService commentsService;

    // 发布动态
    @PostMapping
    public ResponseEntity<?> movements(Movement movement,
                                       MultipartFile imageContent[]) throws Exception{

        movementService.publishMovement(movement,imageContent);
        return ResponseEntity.ok().body(null);
    }

    // 查询我的动态
    @GetMapping("/all")
    public ResponseEntity<PageResult> findByUserId(Long userId,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer pagesize) {
        PageResult pr = movementService.findByUserId(userId,page,pagesize);
        return ResponseEntity.ok().body(pr);
    }

    // 查询好友动态
    @GetMapping
    public ResponseEntity<PageResult> movements(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pagesize) {
        PageResult pr = movementService.findFriendMovements(page,pagesize);
        return ResponseEntity.ok().body(pr);
    }


    // 查询推荐动态
    @GetMapping("/recommend")
    public ResponseEntity<PageResult> recommend(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pagesize) {
        PageResult pr = movementService.findRecommendMovements(page,pagesize);
        return ResponseEntity.ok().body(pr);
    }

    // 单条用户详情
    @GetMapping("/{id}")
    public ResponseEntity<MovementsVo> findById(@PathVariable("id") String movementId) {
        MovementsVo vo = movementService.findById(movementId);
        log.info("{}",movementId);
        return ResponseEntity.ok().body(vo);
    }

    // 点赞动态
    @GetMapping("/{id}/like")
    public ResponseEntity<Integer> like(@PathVariable("id") String movementId) {
        Integer likeCount = commentsService.likeComment(movementId);
        return ResponseEntity.ok().body(likeCount);
    }

    // 取消点赞动态
    @GetMapping("/{id}/dislike")
    public ResponseEntity<Integer> dislike(@PathVariable("id") String movementId) {
        Integer likeCount = commentsService.dislikeComment(movementId);
        return ResponseEntity.ok().body(likeCount);
    }

    // 喜欢动态
    @GetMapping("/{id}/love")
    public ResponseEntity<Integer> love(@PathVariable("id") String movementId) {
        Integer likeCount = commentsService.loveComment(movementId);
        return ResponseEntity.ok().body(likeCount);
    }

    // 取消喜欢动态
    @GetMapping("/{id}/unlove")
    public ResponseEntity<Integer> unlove(@PathVariable("id") String movementId) {
        Integer likeCount = commentsService.disloveComment(movementId);
        return ResponseEntity.ok().body(likeCount);
    }
}
