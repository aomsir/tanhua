package com.aomsir.dubbo.api;

import com.aomsir.dubbo.mappers.UserMapper;
import com.aomsir.model.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@DubboService
public class UserApiImpl implements UserApi{
    @Autowired
    private UserMapper userMapper;


    @Override
    public User findByMobile(String mobile) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("mobile", mobile);
        return userMapper.selectOne(qw);
    }


    @Override
    public Long save(User user) {
        userMapper.insert(user);
        return user.getId();
    }
}
