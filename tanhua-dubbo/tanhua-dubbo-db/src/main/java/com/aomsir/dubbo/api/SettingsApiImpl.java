package com.aomsir.dubbo.api;

import com.aomsir.dubbo.mappers.BlackListMapper;
import com.aomsir.dubbo.mappers.SettingsMapper;
import com.aomsir.model.domain.BlackList;
import com.aomsir.model.domain.Settings;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@DubboService
public class SettingsApiImpl implements SettingsApi{
    @Autowired
    private SettingsMapper settingsMapper;

    @Autowired
    private BlackListMapper blackListMapper;


    @Override
    public Settings findByUserId(Long id) {
        QueryWrapper<Settings> qw = new QueryWrapper<>();
        qw.eq("user_id",id);
        return settingsMapper.selectOne(qw);
    }

    @Override
    public void save(Settings settings) {
        settingsMapper.insert(settings);
    }

    @Override
    public void update(Settings settings) {
        settingsMapper.updateById(settings);
    }

    @Override
    public void delete(Long userId, Long blackUserId) {
        QueryWrapper<BlackList> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        qw.eq("black_user_id",blackUserId);
        blackListMapper.delete(qw);
    }
}
