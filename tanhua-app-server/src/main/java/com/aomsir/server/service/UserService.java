package com.aomsir.server.service;

import com.aomsir.autoconfig.template.SmsTemplate;
import com.aomsir.commons.utils.JwtUtils;
import com.aomsir.dubbo.api.UserApi;
import com.aomsir.model.domain.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
public class UserService {
    private final String redisPrefix = "CHECK_CODE_";

    @Autowired
    private SmsTemplate template;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @DubboReference
    private UserApi userApi;

    /**
     * 发送短信验证码
     * @param phone
     */
    public void sendMsg(String phone) {
        // 1.生成随机6位验证码
         String code = RandomStringUtils.randomNumeric(6);

        // 2.调用template对象发送短信
          template.sendSms(phone,code);

        // 3.将验证码存入Redis,5分钟失效
        redisTemplate.opsForValue().set(redisPrefix+phone,code, Duration.ofMinutes(5));
    }

    /**
     * 验证登录
     * @param phone
     * @param code
     * @return
     */
    public Map loginVerification(String phone, String code) {
        // 1.从Redis中获取下发的验证码
        String redisCode = redisTemplate.opsForValue().get(redisPrefix + phone);

        // 2.对验证码进行校验
        if (StringUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            // 验证码无效
            throw new RuntimeException("验证码无效");
        }

        // 3.删除Redis中的验证码
        redisTemplate.delete(redisPrefix+phone);

        // 4.通过手机号查询用户
        User user = userApi.findByMobile(phone);
        boolean isNew = false;

        // 5.如果用户不存在则创建保存
        if (user == null) {
            user = new User();
            user.setMobile(phone);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            user.setPassword(DigestUtils.md2Hex("123456"));
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;
        }

        // 6.通过JWT生成token
        Map tokenMap = new HashMap();
        tokenMap.put("id",user.getId());
        tokenMap.put("mobile",phone);
        String token = JwtUtils.getToken(tokenMap);

        // 7.构造返回值
        Map retMap = new HashMap();
        retMap.put("token",token);
        retMap.put("isNew",isNew);

        return retMap;
    }
}
