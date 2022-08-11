package com.gpchen.blog.util;

import com.gpchen.blog.model.entity.SysUser;

/**
 * used the ThreadLocal to store current thread,
 * <key, value> -- key: current thread; value: sysUser
 */
public class UserThreadLocal {
    private UserThreadLocal(){}
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
