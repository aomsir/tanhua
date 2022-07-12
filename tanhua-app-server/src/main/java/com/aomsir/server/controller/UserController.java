package com.aomsir.server.controller;

import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.server.service.UserInfoService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo,
                                       @RequestHeader("Authorization") String token) {
        log.info(token);

        // 1.判断token是否合法
        boolean verifyToken = JwtUtils.verifyToken(token);

        log.info("{}",verifyToken);
        if (!verifyToken) {
            return ResponseEntity.status(401).body(null);
        }

        // 2.向userInfo中设置用户id
        Claims claims = JwtUtils.getClaims(token);
        Integer id = (Integer) claims.get("id");
        userInfo.setId(Long.valueOf(id));

        // 3.调用service
        userInfoService.save(userInfo);
        return ResponseEntity.ok().body(null);
    }
}
