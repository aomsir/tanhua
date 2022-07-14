package com.aomsir.server.controller;

import com.aomsir.model.dto.RecommendUserDto;
import com.aomsir.model.vo.PageResult;
import com.aomsir.model.vo.TodayBest;
import com.aomsir.server.service.TanhuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@RestController
@RequestMapping("/tanhua")
public class TanhuaController {

    @Autowired
    private TanhuaService tanhuaService;

    // 查询今日佳人
    @GetMapping("/todayBest")
    public ResponseEntity<TodayBest> todayBest() {
        TodayBest vo = tanhuaService.todayBest();
        return ResponseEntity.ok().body(vo);
    }


    /**
     * 查询分页推荐好友
     * @param dto
     * @return
     */
    @GetMapping("/recommendation")
    public ResponseEntity<PageResult> recommendation(RecommendUserDto dto) {
        PageResult pr = tanhuaService.recommendation(dto);
        return ResponseEntity.ok().body(pr);
    }
}
