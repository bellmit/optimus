package com.optimus.manager.gateway;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.File;
import java.io.IOException;

/**
 * @author hongp
 */
public class GroovyIntoJavaDemo {


    public static void main(String[] args) throws IOException, ResourceException, ScriptException {

        /*
        GroovyClassLoader
         */
        GroovyClassLoader loader = new GroovyClassLoader();
        Class aClass = loader.parseClass(new File("/Users/zhouhonglin/work/groovy/Demo.groovy"));
        try {
            GroovyObject instance = (GroovyObject) aClass.newInstance();
            instance.invokeMethod("getExecuteScriptJson", null);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        /*
        GroovyShell
         */
        new GroovyShell().parse(new File("/Users/zhouhonglin/work/groovy/Demo.groovy"))
                .invokeMethod("getExecuteScriptJson", null);

        /*
        GroovyScriptEngine
         */
        Class script = new GroovyScriptEngine("/Users/zhouhonglin/work/groovy/")
                .loadScriptByName("Demo.groovy");
        try {
            Script instance = (Script) script.newInstance();
            instance.invokeMethod("getExecuteScriptJson", new Object[]{});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}