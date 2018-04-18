package com.netease.nim.uikit.api.wrapper;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.activity.ToolBarOptions;

/**
 * Created by hzxuwen on 2016/6/16.
 */
public class NimToolBarOptions extends ToolBarOptions {

    public NimToolBarOptions() {
//        logoId = R.drawable.nim_actionbar_nest_dark_logo;//logo小图标
//        navigateId = R.drawable.nim_actionbar_dark_back_icon;//返回按钮
        navigateId = R.drawable.icon_back_2;
        isNeedNavigate = true;
    }
}
