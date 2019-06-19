package com.bakon.android.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.bakon.android.R;
import com.bakon.android.utils.UIUtils;

import java.util.List;


/**
 * Created by Tang on 2017/11/15.
 */

public class CommonBottomPopupView extends PopupWindow implements PopupWindow.OnDismissListener {
    private Context mContext;
    private CommOnClickListener mListener;

    private TextView tvCancle;
    private RecyclerView recyclerView;

    private List mData;

    public CommonBottomPopupView(Context context, List data, CommOnClickListener commOnClickListener) {
        this.mContext = context;
        this.mData = data;
        this.mListener = commOnClickListener;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_bottom_view, null);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        recyclerView = view.findViewById(R.id.comm_recycleView);
        tvCancle = view.findViewById(R.id.comm_cancle);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BaseQuickAdapter adapter = new BaseQuickAdapter(R.layout.common_bottom_item, mData) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.tv_comm_item, item.toString());
            }
        };
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                if (mListener != null) {
                    mListener.OnItemOnClick(mData.get(position));
                }
            }
        });
        this.setContentView(view);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(((Activity) mContext));
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onDismiss() {
        UIUtils.lightOn((Activity) mContext);
    }


    public interface CommOnClickListener {
        void OnItemOnClick(Object o);
    }

}
