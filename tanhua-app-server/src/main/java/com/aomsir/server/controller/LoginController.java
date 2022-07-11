package com.aomsir.server.controller;

import com.aomsir.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param map
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map map) {
        String phone = (String) map.get("phone");
        userService.sendMsg(phone);
        // ResponseEntity.status(500).body("出错啦！");
        return ResponseEntity.ok(null);
    }


    /**
     * 校验登录
     * @param map
     * @return
     */
    @PostMapping("/loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map map) {
        // 1.调用map集合获取请求参数
        String phone = (String) map.get("phone");
        String code = (String) map.get("verificationCode");

        // 2.调用userService完成用户登录
        Map retMap = userService.loginVerification(phone,code);

        // 3.构造返回
        return ResponseEntity.ok(retMap);
    }
}
