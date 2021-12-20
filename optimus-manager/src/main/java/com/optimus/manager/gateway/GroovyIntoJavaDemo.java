package com.optimus.manager.gateway;

import java.math.BigDecimal;
import java.util.Date;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.util.GenerateUtil;
import com.optimus.util.JacksonUtil;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

/**
 * @author hongp
 */
public class GroovyIntoJavaDemo {

    public static void main(String[] args) {

        try {

            GroovyScriptEngine engine = new GroovyScriptEngine("/Users/admin/Workspace/Java/optimus");

            ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();
            input.setScriptMethod("create");
            input.setOrderId(GenerateUtil.generate("Z"));
            input.setCalleeOrderId(GenerateUtil.generate("Z"));
            input.setAmount(new BigDecimal("100"));
            input.setOrderTime(new Date());
            input.setBizContent(
                    "{\"channelMerchantId\":574306,\"channelMerchantKey\":\"45b066904eb6cd4a5fe3c10bc7e070fd\",\"channelCode\":100,\"callbackUrl\":\"https://www.baidu.com?subChannelCode=100\",\"redirectUrl\":\"https://www.google.com\",\"createOrderUrl\":\"http://150.109.121.163:9000/api/Pay/Create\",\"queryOrderUrl\":\"http://150.109.121.163:9000/api/Core/Order/GetOrderInfo\"}");

            Binding b = new Binding();
            b.setVariable("input", JacksonUtil.toString(input));
            Object create = engine.run("Template.groovy", b);

            ExecuteScriptOutputDTO output = JacksonUtil.toBean((String) create, ExecuteScriptOutputDTO.class);
            System.out.println("create:" + output);

            input = new ExecuteScriptInputDTO();
            input.setScriptMethod("query");
            input.setOrderId(output.getOrderId());
            input.setCalleeOrderId(output.getCalleeOrderId());
            input.setBizContent(
                    "{\"channelMerchantId\":574306,\"channelMerchantKey\":\"45b066904eb6cd4a5fe3c10bc7e070fd\",\"channelCode\":100,\"callbackUrl\":\"https://www.baidu.com?subChannelCode=100\",\"redirectUrl\":\"https://www.google.com\",\"createOrderUrl\":\"http://150.109.121.163:9000/api/Pay/Create\",\"queryOrderUrl\":\"http://150.109.121.163:9000/api/Core/Order/GetOrderInfo\"}");

            b = new Binding();
            b.setVariable("input", JacksonUtil.toString(input));
            Object query = engine.run("Template.groovy", b);

            output = JacksonUtil.toBean((String) query, ExecuteScriptOutputDTO.class);
            System.out.println("query:" + output);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}