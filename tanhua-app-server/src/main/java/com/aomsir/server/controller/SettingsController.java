package com.aomsir.server.controller;

import com.aomsir.model.vo.PageResult;
import com.aomsir.model.vo.SettingsVo;
import com.aomsir.server.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@RestController
@RequestMapping("/users")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    /**
     * 获取设置信息
     * @return
     */
    @GetMapping("/settings")
    public ResponseEntity<SettingsVo> settings() {
        SettingsVo vo = settingsService.settings();
        return ResponseEntity.ok().body(vo);
    }

    /**
     * 陌生人问题设置
     * @param map
     * @return
     */
    @PostMapping("/questions")
    public ResponseEntity<Object> questions(@RequestBody Map<String,String> map) {
        // 1.获取参数
        String content = (String) map.get("content");
        settingsService.saveQuestions(content);

        return ResponseEntity.ok().body(null);
    }

    /**
     * 通知设置
     * @param map
     * @return
     */
    @PostMapping("/notifications/setting")
    public ResponseEntity<Object> notifications(@RequestBody Map<String,Boolean> map) {
        settingsService.saveSettings(map);
        return ResponseEntity.ok().body(null);
    }


    /**
     * 查询黑名单列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/blacklist")
    public ResponseEntity<PageResult> blacklist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 1.调用service进行查询
        PageResult pr = settingsService.blacklist(page,size);

        // 2.构造返回
        return ResponseEntity.ok().body(pr);
    }


    @DeleteMapping("/blacklist/{uid}")
    public ResponseEntity<?> deleteBlackList(@PathVariable("uid") Long blackUserId) {
        settingsService.deleteBlackList(blackUserId);
        return ResponseEntity.ok().body(null);
    }
}
