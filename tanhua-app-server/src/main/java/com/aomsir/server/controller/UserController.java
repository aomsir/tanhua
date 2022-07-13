package com.aomsir.server.controller;

import com.aomsir.model.domain.UserInfo;
import com.aomsir.server.interceptor.UserHolder;
import com.aomsir.server.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * @return
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity<Object> loginReginfo(@RequestBody UserInfo userInfo) {


        // 1.向userInfo中设置用户id
        userInfo.setId(UserHolder.getUserId());

        // 2.调用service
        userInfoService.save(userInfo);
        return ResponseEntity.ok().body(null);
    }


    /**
     * 上传用户头像
     * @param headPhoto
     * @return
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity<Object> head(MultipartFile headPhoto) throws IOException {

        // 1.调用service
        userInfoService.updateHead(headPhoto,UserHolder.getUserId());

        return ResponseEntity.ok().body(null);
    }
}
