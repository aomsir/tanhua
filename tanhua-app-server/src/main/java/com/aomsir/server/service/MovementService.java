package com.aomsir.server.service;

import cn.hutool.core.collection.CollUtil;
import com.aomsir.autoconfig.template.OssTemplate;
import com.aomsir.commons.utils.Constants;
import com.aomsir.dubbo.api.MovementApi;
import com.aomsir.dubbo.api.UserInfoApi;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.mongo.Movement;
import com.aomsir.model.vo.ErrorResult;
import com.aomsir.model.vo.MovementsVo;
import com.aomsir.model.vo.PageResult;
import com.aomsir.server.exception.BusinessException;
import com.aomsir.server.interceptor.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
public class MovementService {
    @Autowired
    private OssTemplate ossTemplate;

    @DubboReference
    private MovementApi movementApi;

    @DubboReference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 发布动态
     * @param movement
     * @param imageContent
     */
    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        // 1.判断发布的动态内容是否存在
        if (StringUtils.isEmpty(movement.getTextContent())) {
            throw new BusinessException(ErrorResult.contentError());
        }

        // 2.获取当前登录的用户ID
        Long userId = UserHolder.getUserId();

        // 3.将文件上传到阿里云OSS,获取请求地址
        List<String> medias = new ArrayList<>();
        for (MultipartFile multipartFile : imageContent) {
            String update = ossTemplate.update(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            medias.add(update);
        }

        // 4.将数据封装到Movement
        movement.setUserId(userId);
        movement.setMedias(medias);

        // 5.调用API完成动态发布
        movementApi.publish(movement);

    }


    // 查询我的动态
    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {
        // 1.根据userId调用API查询个人动态内容 (PageResult  -- Movement)
        PageResult pr = movementApi.findByUserId(userId,page,pagesize);

        // 2.获取PageResult中的item列表对象
        List<Movement> items = (List<Movement>) pr.getItems();

        // 3.非空判断
        if (items == null) {
            return pr;
        }

        // 4.循环数据列表
        UserInfo userInfo = userInfoApi.findById(userId);
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement item : items) {
            // 5.一个Movement对象构建一个Vo对象
            MovementsVo vo = MovementsVo.init(userInfo, item);
            vos.add(vo);
        }

        // 6.构建返回值
        pr.setItems(vos);
        return pr;
    }

    // 查询好友动态
    public PageResult findFriendMovements(Integer page, Integer pagesize) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUserId();

        // 2.调用API获取好友动态详情列表
        List<Movement> list = movementApi.findFriendMovements(page,pagesize,userId);

        // 3.列表判空
        return getPageResult(page, pagesize, list);
    }

    // 复用方法
    private PageResult getPageResult(Integer page, Integer pagesize, List<Movement> list) {
        if (list == null || list.isEmpty()) {
            return new PageResult();
        }

        // 4.提取动态发布人的id列表
        List<Long> userIds = CollUtil.getFieldValues(list, "userId", Long.class);

        // 5.根据用户id列表获取用户详情
        Map<Long, UserInfo> userInfoMap = userInfoApi.findByIds(userIds, null);

        // 6.一个Movements构造一个vo对象
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement movement : list) {
            UserInfo userInfo = userInfoMap.get(movement.getUserId());
            if (userInfo != null) {
                MovementsVo vo = MovementsVo.init(userInfo, movement);

                // 点赞状态bug,判断hashKey是否存在
                String key = Constants.MOVEMENTS_INTERACT_KEY + movement.getId().toHexString();
                String hashKey = Constants.MOVEMENT_LIKE_HASHKEY +UserHolder.getUserId();
                if (redisTemplate.opsForHash().hasKey(key,hashKey)) {
                    vo.setHasLiked(1);
                }

                String loveHashKey = Constants.MOVEMENT_LOVE_HASHKEY +UserHolder.getUserId();
                if (redisTemplate.opsForHash().hasKey(key,loveHashKey)) {
                    vo.setHasLoved(1);
                }

                vos.add(vo);
            }
        }

        // 7.构造PageResult并返回
        return new PageResult(page, pagesize, 0l, vos);
    }

    // 查询推荐动态
    public PageResult findRecommendMovements(Integer page, Integer pagesize) {
        // 1.从Redis中获取推荐数据
        String redisKey = Constants.MOVEMENTS_RECOMMEND + UserHolder.getUserId();
        String redisValue = redisTemplate.opsForValue().get(redisKey);

        // 2.判断推荐数据是否存在
        List<Movement> list = Collections.EMPTY_LIST;
        if (StringUtils.isEmpty(redisValue)) {
            // 3.如果不存在,调用API随机构造10条动态数据
            list = movementApi.randomMovements(pagesize);
        } else {
            // 4.如果存在,处理PID数据 10020,28,10092,24,25,27,10064,26,10067,20,16,10099,19,10015,10040,10093,18,17,22,10081
            String[] values = redisValue.split(",");
            if ((page - 1)*pagesize < values.length) {
                List<Long> pids = Arrays.stream(values).skip((page - 1) * pagesize).limit(pagesize)
                        .map(e -> Long.valueOf(e))
                        .collect(Collectors.toList());
                // 5.调用API根据PID集合查询动态数据
                list = movementApi.findMovementsByPids(pids);
            }
        }

        // 6.调用复用方法返回
        return getPageResult(page,pagesize,list);
    }

    public MovementsVo findById(String movementId) {
        // 1.调用api根据id查询动态详情
        Movement movement = movementApi.findById(movementId);

        // 2.转化为VO对象返回
        if (movement != null) {
            UserInfo userInfo = userInfoApi.findById(movement.getUserId());
            return MovementsVo.init(userInfo,movement);
        } else {
            return null;
        }
    }
}
