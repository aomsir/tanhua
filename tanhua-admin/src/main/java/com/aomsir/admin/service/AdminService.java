package com.aomsir.admin.service;

import com.aomsir.admin.mapper.AdminMapper;
import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.model.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

}
