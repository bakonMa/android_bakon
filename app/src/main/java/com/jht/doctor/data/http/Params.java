package com.jht.doctor.data.http;

import java.util.HashMap;

/**
 * Created by table on 2017/11/24.
 * description:
 */

public class Params extends HashMap<String,Object> {
    public Params(){
        //todo 公共请求参数
        put("timestamp", System.currentTimeMillis());
        put("sign", "3d1f1eb1e56fec2bbdd6436bdf333ce1");

    }
}
