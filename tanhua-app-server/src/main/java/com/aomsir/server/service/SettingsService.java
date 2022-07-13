package com.aomsir.server.service;

import com.aomsir.dubbo.api.BlackListApi;
import com.aomsir.dubbo.api.QuestionApi;
import com.aomsir.dubbo.api.SettingsApi;
import com.aomsir.model.domain.Question;
import com.aomsir.model.domain.Settings;
import com.aomsir.model.domain.UserInfo;
import com.aomsir.model.vo.PageResult;
import com.aomsir.model.vo.SettingsVo;
import com.aomsir.server.interceptor.UserHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Service
public class SettingsService {
    @DubboReference
    private QuestionApi questionApi;

    @DubboReference
    private SettingsApi settingsApi;

    @DubboReference
    private BlackListApi blackListApi;


    /**
     * 查询通用设置
     * @return
     */
    public SettingsVo settings() {
        // 1.获取用户id以及手机数据
        SettingsVo vo = new SettingsVo();
        Long id = UserHolder.getUserId();

        vo.setId(id);
        vo.setPhone(UserHolder.getUserMobile());

        // 2.获取用户的陌生人问题
        Question question = questionApi.findByUserId(id);
        String txt = question == null?"你喜欢Java吗?":question.getTxt();
        vo.setStrangerQuestion(txt);

        // 3.获取用户的APP通知开关数据
        Settings settings = settingsApi.findByUserId(id);
        if (settings != null) {
            vo.setGonggaoNotification(settings.getGonggaoNotification());
            vo.setLikeNotification(settings.getLikeNotification());
            vo.setPinglunNotification(settings.getPinglunNotification());
        }

        return vo;
    }

    /**
     * 设置陌生人问题
     * @param content
     */
    public void saveQuestions(String content) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUserId();

        // 2.调用API查询当前用户的陌生人问题
        Question question = questionApi.findByUserId(userId);

        // 3.判断问题是否存在,不存在则保存,存在则更新
        if (question == null) {
            Question question1 =new Question();
            question1.setUserId(userId);
            question1.setTxt(content);
            questionApi.save(question1);
        } else {
            question.setUserId(userId);
            question.setTxt(content);
            questionApi.update(question);
        }
    }

    /**
     * 设置通知
     * @param map
     */
    public void saveSettings(Map<String, Boolean> map) {
        boolean likeNotification = map.get("likeNotification");
        boolean pinglunNotification = map.get("pinglunNotification");
        boolean gonggaoNotification = map.get("gonggaoNotification");

        // 1.获取当前用户id
        Long userId = UserHolder.getUserId();

        // 2.查询当前用户通知设置
        Settings settings = settingsApi.findByUserId(userId);

        // 3.进行判断
        if (settings == null) {
            settings = new Settings();
            settings.setUserId(userId);
            settings.setGonggaoNotification(gonggaoNotification);
            settings.setLikeNotification(likeNotification);
            settings.setPinglunNotification(pinglunNotification);
            settingsApi.save(settings);
        } else {
            settings.setUserId(userId);
            settings.setGonggaoNotification(gonggaoNotification);
            settings.setLikeNotification(likeNotification);
            settings.setPinglunNotification(pinglunNotification);
            settingsApi.update(settings);
        }
    }

    /**
     * 分页查询黑名单
     * @param page
     * @param size
     * @return
     */
    public PageResult blacklist(int page, int size) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUserId();

        // 2.调用API查询当前用户黑名单分页列表
        IPage<UserInfo> iPage = blackListApi.findByUserId(userId,page,size);

        // 3.对象转换
        PageResult pr = new PageResult(page,size,iPage.getTotal(),iPage.getRecords());


        // 4.返回
        return pr;
    }

    /**
     * 取消黑名单
     * @param blackUserId
     */
    public void deleteBlackList(Long blackUserId) {
        // 1.获取当前用户ID
        Long userId = UserHolder.getUserId();

        // 2.调用API
        settingsApi.delete(userId,blackUserId);
    }
}
