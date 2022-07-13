package com.aomsir.dubbo.api;

import com.aomsir.dubbo.mappers.BlackListMapper;
import com.aomsir.dubbo.mappers.UserInfoMapper;
import com.aomsir.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class BlackListApiImpl implements BlackListApi{
    @Autowired
    private BlackListMapper blackListMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public IPage<UserInfo> findByUserId(Long userId, int page, int size) {
        // 构建自定义分页对象
        Page pages = new Page(page,size);

        // 调用方法分页
        return blackListMapper.findBlackList(pages,userId);
    }
}
