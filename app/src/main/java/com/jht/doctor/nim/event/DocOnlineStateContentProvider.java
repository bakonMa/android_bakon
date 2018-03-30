package com.jht.doctor.nim.event;

import android.text.TextUtils;

import com.jht.doctor.nim.DocCache;
import com.netease.nim.uikit.api.model.main.OnlineStateContentProvider;

/**
 * Created by hzchenkang on 2017/3/31.
 */

public class DocOnlineStateContentProvider implements OnlineStateContentProvider {

    @Override
    public String getSimpleDisplay(String account) {
        String content = getDisplayContent(account, true);
        if (!TextUtils.isEmpty(content)) {
            content = "[" + content + "]";
        }
        return content;
    }

    @Override
    public String getDetailDisplay(String account) {
        return getDisplayContent(account, false);
    }

    private String getDisplayContent(String account, boolean simple) {
        if (account == null || account.equals(DocCache.getAccount())) {
            return "";
        }

        // 被过滤掉的直接显示在线，如机器人
        if (OnlineStateEventSubscribe.subscribeFilter(account)) {
            return "在线";
        }

        // 检查是否订阅过
        OnlineStateEventManager.checkSubscribe(account);

        OnlineState onlineState = OnlineStateEventCache.getOnlineState(account);
        return OnlineStateEventManager.getOnlineClientContent(DocCache.getContext(), onlineState, simple);
    }
}
