package com.optimus.util.global.exception;

import java.util.Objects;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.global.resp.Resp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionAdvice
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * exception for OptimusException
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

        log.warn("warn resp is {}", resp);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

    /**
     * exception for Exception
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Resp<RespCodeEnum>> exception(Exception e) {

        Resp<RespCodeEnum> resp = new Resp<RespCodeEnum>();
        resp.setCode(RespCodeEnum.FAILE.getCode());
        resp.setMemo(RespCodeEnum.FAILE.getMemo());

        log.error("error resp is {}", e);

        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

}
