package com.aomsir.server.controller;

import com.aomsir.model.vo.PageResult;
import com.aomsir.server.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/22
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentService;

    // 发布评论
    @PostMapping
    public ResponseEntity<?> saveComments(@RequestBody Map<String,String> map) {
        String movementId = map.get("movementId");
        String comment = map.get("comment");
        commentService.saveComments(movementId,comment);
        return ResponseEntity.ok().body(null);
    }

    // 分页查询评论列表
    @GetMapping
    public ResponseEntity<PageResult> findComments(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "1") Integer pageSize,
                                         String movementId) {
        PageResult pr = commentService.findComments(movementId,page,pageSize);
        return ResponseEntity.ok().body(pr);
    }

}
