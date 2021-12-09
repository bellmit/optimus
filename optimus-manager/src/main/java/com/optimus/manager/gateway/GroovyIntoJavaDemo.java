package com.optimus.manager.gateway;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

/**
 * @author hongp
 */
public class GroovyIntoJavaDemo {

    public static void main(String[] args) {

        try {

            GroovyScriptEngine engine = new GroovyScriptEngine("/Users/admin/Appspace/optimus/groovy");

            Binding binding = new Binding();
            binding.setVariable("codeMemberId", "hello");

            String scriptName = "demo.groovy";

            Object result = engine.run(scriptName, binding);
            System.out.println("-----" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}