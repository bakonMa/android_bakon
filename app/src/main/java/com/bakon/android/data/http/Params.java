package com.bakon.android.data.http;

import com.bakon.android.config.HttpConfig;
import com.bakon.android.utils.MD5Util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Params
 * Create by mayakun at 2018/3/27 下午1:51
 */

public class Params extends HashMap<String, Object> {
    public Params() {
        put(HttpConfig.TIMESTAMP, System.currentTimeMillis());
    }

    //获取sign
    public String getSign(Params params) {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, Object> map = sortMapByKey(params);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(null == entry.getValue() ? "" : entry.getValue().toString())
                    .append("&");
        }
        stringBuffer.append("secret=").append(HttpConfig.SECRET_VALUE);
        return MD5Util.md5(stringBuffer.toString());

    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}

class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
