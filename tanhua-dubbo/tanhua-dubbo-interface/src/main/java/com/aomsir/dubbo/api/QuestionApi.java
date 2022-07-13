package com.aomsir.dubbo.api;

import com.aomsir.model.domain.Question;

public interface QuestionApi {
    // 根据id查询陌生人信息
    Question findByUserId(Long id);

    // 保存用户陌生人问题
    void save(Question question1);

    // 更新用户陌生人问题
    void update(Question question);
}
