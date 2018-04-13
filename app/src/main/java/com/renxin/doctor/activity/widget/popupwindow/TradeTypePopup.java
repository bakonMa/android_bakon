package com.renxin.doctor.activity.widget.popupwindow;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.widget.GridSpacingItemDecoration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mayakun on 2017/12/14
 */

public class TradeTypePopup extends PopupWindow {
    private Activity activity;

    private SelectTypeCallBack callBack;
    private int current = 0;
    private RecyclerView recyclerView;
    private List<String> typeList = Arrays.asList("全部", "充值", "提现", "交易成功", "交易失败", "在途中");

    public TradeTypePopup(Activity activity, SelectTypeCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.dir_popupwindow_anim_top);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x55000000);
        this.setBackgroundDrawable(dw);
    }


    private void initView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_trade_detail, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        view.findViewById(R.id.id_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 0, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        BaseQuickAdapter adapter = new BaseQuickAdapter(R.layout.item_trade_type, typeList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.id_cb, item.toString())
                        .setVisible(R.id.type_lines, typeList.indexOf(item) <= 2)
                        .setChecked(R.id.id_cb, typeList.indexOf(item) == current);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                current = position;
                adapter.notifyDataSetChanged();
                callBack.selectType(current);
                dismiss();
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        //7.0以后
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom - yoff;
            setHeight(height);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor) {
        //7.0以后
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    //点击回调
    public interface SelectTypeCallBack {
        void selectType(int type);
    }

}
