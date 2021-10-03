package com.example.seckill.exception;

import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandle(Exception ex) {
        log.info("Exception:" + ex.toString());

        if (ex instanceof GlobalException) {
            GlobalException e = (GlobalException) ex;
            return RespBean.error(e.getRespBeanEnum());
        } else if (ex instanceof BindException) {
            BindException e = (BindException) ex;
            String str = e.getAllErrors().get(0).getDefaultMessage();
            RespBean error = RespBean.error(RespBeanEnum.PASSWORD_ERROR);
            error.setMessage("Parameter Exception: " +str);
            return error;
        }
        //这里之前写错了 不要null
        //请返回默认异常
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
