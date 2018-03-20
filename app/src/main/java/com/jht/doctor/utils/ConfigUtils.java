package com.jht.doctor.utils;

import java.util.List;

/**
 * Created by table on 2017/11/29.
 * description:
 */

public class ConfigUtils<T> {
    private static ConfigUtils instanse;

    private ConfigUtils(){

    }

    public static ConfigUtils getInstanse(){
        if (instanse == null) {
            synchronized (ConfigUtils.class) {
                if (instanse == null) {
                    instanse = new ConfigUtils();
                }
            }
        }
        return instanse;
    }
    private List<String> getStrs(List<T> data){
        return null;
    }
}
