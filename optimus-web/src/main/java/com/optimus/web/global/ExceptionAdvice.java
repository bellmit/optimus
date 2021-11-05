package com.optimus.web.global;

import com.optimus.util.constants.RespCodeEnum;
import com.optimus.web.global.exception.OptimusException;
import com.optimus.web.global.resp.Resp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ExceptionAdvice
 */
@Configuration
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * exception for OptimusException
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ OptimusException.class })
    public ResponseEntity<Resp<RespCodeEnum>> exception(OptimusException e) {

        System.out.println("==========");
        Resp<RespCodeEnum> resp = new Resp<RespCodeEnum>();
        resp.setCode(e.getRespCodeEnum().getCode());
        resp.setMemo(e.getRespCodeEnum().getMemo());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);

    }

    /**
     * exception for Exception
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Resp<RespCodeEnum>> exception(Exception e) {

        System.out.println("-=-=");
        Resp<RespCodeEnum> resp = new Resp<RespCodeEnum>();
        resp.setCode(RespCodeEnum.FAILE.getCode());
        resp.setMemo(RespCodeEnum.FAILE.getMemo());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);

    }

}
