package com.example.spring_utioy_v1.utio;
//日志功能
public class Log {


    public static void info(String msg){
        System.out.println(msg);
    }

    public static void error(String msg){
        System.err.println(msg);
    }

    public static void debug(String msg){
        System.out.println(msg);
    }
}
