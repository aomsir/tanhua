package com.aomsir.server.controller;


import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.vo.UserInfoVo;
import com.aomsir.server.interceptor.UserHolder;
import com.aomsir.server.service.UserInfoService;
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

    /**
     * 查询用户信息
     * @param userID
     * @return
     */
    @GetMapping()
    public ResponseEntity<UserInfoVo> users(Long userID) {

        // 判断用户ID
        if (userID == null) {
            userID = UserHolder.getUserId();
        }

        UserInfoVo userInfoVo = userInfoService.findById(userID);

        return ResponseEntity.ok().body(userInfoVo);
    }


    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    @PutMapping()
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo) {

        // 设置用户id
        userInfo.setId(UserHolder.getUserId());
        userInfoService.update(userInfo);

        return ResponseEntity.ok().body(null);
    }

}
