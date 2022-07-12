package com.aomsir.server.controller;

import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.server.interceptor.UserHolder;
import com.aomsir.server.service.UserInfoService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 首次登录---完善资料
     * @param userInfo
     * @param token
     * @return
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo,
                                       @RequestHeader("Authorization") String token) {


        // 2.向userInfo中设置用户id
        userInfo.setId(Long.valueOf(UserHolder.getUserId()));

        // 3.调用service
        userInfoService.save(userInfo);
        return ResponseEntity.ok().body(null);
    }


    /**
     * 上传用户头像
     * @param headPhoto
     * @param token
     * @return
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity head(MultipartFile headPhoto,
                               @RequestHeader("Authorization") String token) throws Exception{


        // 3.调用service
        userInfoService.updateHead(headPhoto,UserHolder.getUserId());

        return ResponseEntity.ok().body(null);
    }
}
