package com.bakon.android.data.localdata;

import com.bakon.android.application.MyApplication;
import com.bakon.android.utils.LogUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVLogLevel;

public class MMKVManager {
    public static MMKV mmkv;

    public MMKVManager(MyApplication application) {
        //默认目录
        String rootDir = MMKV.initialize(application);
        LogUtil.d("debug", "mmkv path=" + rootDir);

//        mmkv = MMKV.defaultMMKV();//默认
        mmkv = MMKV.mmkvWithID("MMKV_bakon");//默认

        //日志
        MMKV.setLogLevel(MMKVLogLevel.LevelDebug);

        //如果不同业务需要区别存储，也可以单独创建自己的实例：
        //MMKV mmkv = MMKV.mmkvWithID("MyID");
        //如果不同业务需要区别存储，也可以单独创建自己的实例：
        //MMKV mmkv = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
    }
}
