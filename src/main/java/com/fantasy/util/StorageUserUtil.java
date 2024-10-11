package com.fantasy.util;


import com.fantasy.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageUserUtil {

    public static Map<Long, User> userMap = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private static User currentUser;


    public static void addUser(User user){
        userMap.put(user.getId(),user);
    }
    public static void addUserAndSetCurrentUser(User user){
        addUser(user);
        setCurrentUser(user);
    }

    public static User getUserByUserId(Long userId){
        return userMap.get(userId);
    }

}
