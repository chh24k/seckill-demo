package com.example.seckill.config;

import com.example.seckill.pojo.User;

public class UserContext {

    private static ThreadLocal<User> threadLocal = new ThreadLocal<User>();

    public static User getUser() {
        return threadLocal.get();
    }

    public static void setUser(User user) {
        threadLocal.set(user);
    }

}
