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

            Binding binding1 = new Binding();
            binding1.setVariable("codeMemberId", "hello");
            System.out.println("-----" + engine.run("demo.groovy", binding1));

            Binding binding2 = new Binding();
            binding2.setVariable("input", "{\"scriptMethod\":\"create\"}");
            System.out.println("-----" + engine.run("Template.groovy", binding2));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}