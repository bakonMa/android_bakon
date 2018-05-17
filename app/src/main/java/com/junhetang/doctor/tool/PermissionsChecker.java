package com.junhetang.doctor.tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
public class PermissionsChecker {

    /**
     * 判断某Context中是否缺少某些权限（或之中的一些）
     * @param context
     * @param permissions
     * @return
     */
    public static Map<String , Boolean> lacksPermissions(Context context , String... permissions){
        Map<String , Boolean> results = new HashMap<>();
        for (String permission : permissions) {
            results.put(permission , lacksPermission(context , permission));
        }
        return results;
    }

    /**
     * 判断某Context中是否缺少某权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean lacksPermission(Context context , String permission){
        return ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_DENIED;
    }

}
