package com.aomsir.dubbo.api;

import com.aomsir.dubbo.mappers.QuestionMapper;
import com.aomsir.model.domain.Question;
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
public class QuestionApiImpl implements QuestionApi{
    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 根据id查陌生人信息
     * @param id
     * @return
     */
    @Override
    public Question findByUserId(Long id) {
        QueryWrapper<Question> qw = new QueryWrapper<>();
        qw.eq("user_id",id);
        return questionMapper.selectOne(qw);
    }

    @Override
    public void save(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public void update(Question question) {
        questionMapper.updateById(question);
    }
}
