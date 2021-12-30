package com.optimus.runner.config;

import java.util.Objects;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.resp.Resp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionConfig
 * 
 * @author sunxp
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class ExceptionConfig {

    /**
     * 业务异常
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ OptimusException.class })
    public ResponseEntity<Resp<RespCodeEnum>> exception(OptimusException e) {

        String code = e.getRespCodeEnum().getCode();
        String memo = e.getRespCodeEnum().getMemo();

        if (!Objects.isNull(e.getMemo())) {
            memo = e.getMemo();
        }

        Resp<RespCodeEnum> resp = new Resp<RespCodeEnum>();
        resp.setCode(code);
        resp.setMemo(memo);

        log.warn("自定义业务异常:{}", resp);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

    /**
     * 系统异常
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Resp<RespCodeEnum>> exception(Exception e) {

        Resp<RespCodeEnum> resp = new Resp<RespCodeEnum>();
        resp.setCode(RespCodeEnum.FAILE.getCode());
        resp.setMemo(RespCodeEnum.FAILE.getMemo());

        log.error("系统服务异常:", e);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

}
