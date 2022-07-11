package com.aomsir.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BasePojo {
    private Long id;
    private String mobile;
    private String password;

}
