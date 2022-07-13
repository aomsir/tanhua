package com.aomsir.dubbo.mappers;

import com.aomsir.model.domain.BlackList;
import com.aomsir.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlackListMapper extends BaseMapper<BlackList> {
    @Select("SELECT * FROM tb_user_info WHERE id IN (\n" +
            "\tSELECT black_user_id FROM tb_black_list WHERE user_id = #{userId}\n" +
            ")")
    IPage<UserInfo> findBlackList(@Param("pages") Page pages,
                                  @Param("userId") Long userId);
}
