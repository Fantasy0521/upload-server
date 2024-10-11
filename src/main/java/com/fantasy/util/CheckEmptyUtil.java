package com.fantasy.util;

public class CheckEmptyUtil {

    /**
     * 有一个属性为空就返回false
     * @param objects
     * @return
     */
    public static boolean checkIfAnyNullOrEmpty(Object... objects) {
        for (Object obj : objects) {
            if (obj == null || (obj instanceof String && ((String) obj).isEmpty())) {
                return false;
            }
        }
        return true;
    }

}
