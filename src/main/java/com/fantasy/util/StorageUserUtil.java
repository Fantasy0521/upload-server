package com.fantasy.util;


import com.fantasy.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageUserUtil {

    public static Map<Long, User> userMap = new HashMap<>();

    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();


    public static void addUser(Long userId,User user){
        userMap.put(userId,user);
    }

    public static User getCurrentUser(){
        return threadLocal.get();
    }
    public static void setCurrentUser(User user){
        threadLocal.set(user);
        userMap.put(user.getId(),user);
    }

    public static User getUserByUserId(Long userId){
        return userMap.get(userId);
    }

}
