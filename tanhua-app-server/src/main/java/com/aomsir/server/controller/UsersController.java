package com.aomsir.server.controller;

import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.vo.UserInfoVo;
import com.aomsir.server.interceptor.UserHolder;
import com.aomsir.server.service.UserInfoService;
import io.jsonwebtoken.Claims;
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

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping()
    public ResponseEntity users(@RequestHeader("Authorization") String token,
                                Long userID) {

        // 判断用户ID
        if (userID == null) {
            userID = UserHolder.getUserId();
        }

        UserInfoVo userInfo = userInfoService.findById(userID);

        return ResponseEntity.ok().body(userInfo);
    }


    @PutMapping()
    public ResponseEntity updateUserInfo(@RequestHeader("Authorization") String token,
                                         @RequestBody UserInfo userInfo) {

        // 设置用户id
        userInfo.setId(UserHolder.getUserId());
        userInfoService.update(userInfo);

        return ResponseEntity.ok().body(null);
    }

}
