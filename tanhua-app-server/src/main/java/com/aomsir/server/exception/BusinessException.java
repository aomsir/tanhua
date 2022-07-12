package com.aomsir.server.exception;

import com.aomsir.model.vo.ErrorResult;
import lombok.Data;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description: 自定义异常
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Data
public class BusinessException extends RuntimeException{
    private ErrorResult errorResult;

    public BusinessException(ErrorResult errorResult) {
        super(errorResult.getErrMessage());
        this.errorResult = errorResult;
    }
}
