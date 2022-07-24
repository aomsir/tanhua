package com.aomsir.admin.controller;

import com.aomsir.admin.service.AdminService;
import com.aomsir.commons.utils.Constants;
import com.aomsir.model.domain.Admin;
import com.aomsir.model.vo.AdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/users")
public class SystemController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

}
