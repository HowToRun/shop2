package com.huaxin.xxshop.util;


public class UserThreadLocal {
    private static  ThreadLocal<String> userThread = new ThreadLocal<>();

    public static void set(String userId){
        userThread.set(userId);
    }

    public static String get(){

        return userThread.get();
    }

    //防止内存泄漏
    public static void remove(){

        userThread.remove();
    }
}
