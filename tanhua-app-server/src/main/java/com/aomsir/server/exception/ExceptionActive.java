package com.aomsir.server.exception;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description: 自定义统一异常处理,不再使用try-catch
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

import com.aomsir.model.vo.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionActive {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResult> handlerException(BusinessException be) {
        ErrorResult errorResult = be.getErrorResult();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handlerException1(Exception be) {
        be.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error());
    }
}
