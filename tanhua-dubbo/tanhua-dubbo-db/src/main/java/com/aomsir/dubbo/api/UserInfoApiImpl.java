package com.aomsir.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.aomsir.dubbo.mappers.UserInfoMapper;
import com.aomsir.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@DubboService
public class UserInfoApiImpl implements UserInfoApi{
    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(Long id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public Map<Long, UserInfo> findByIds(List<Long> userIds, UserInfo info) {
        QueryWrapper qw = new QueryWrapper();

        // 1.用户id列表
        qw.in("id",userIds);

        // 2.添加筛选条件
        if (info != null) {
            if (info.getAge() != null) {
                qw.lt("age",info.getAge());
            }
            if (!StringUtils.isEmpty(info.getGender())) {
                qw.eq("gender",info.getGender());
            }
        }

        // 3.查询数据库
        List<UserInfo> list = userInfoMapper.selectList(qw);

        // 4.构造返回
        Map<Long, UserInfo> map = CollUtil.fieldValueMap(list, "id");
        return map;
    }
}
