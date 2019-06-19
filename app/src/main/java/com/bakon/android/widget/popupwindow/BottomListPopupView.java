package com.bakon.android.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bakon.android.R;
import com.bakon.android.utils.KeyBoardUtils;
import com.bakon.android.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * BottomListPopupView
 * Create at 2018/8/23 下午4:53 by mayakun
 */
public class BottomListPopupView<T> extends PopupWindow {
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private Context mContext;
    private String title;
    private List<T> textList;
    private BaseQuickAdapter mAdapter;
    private OnClickListener mListener;


    public BottomListPopupView(Context context, List<T> textList, OnClickListener onClickListener) {
        this(context, "请选择", textList, onClickListener);
    }

    public BottomListPopupView(Context context, String title, List<T> textList, OnClickListener onClickListener) {
        this.mContext = context;
        this.title = title;
        this.textList = textList;
        this.mListener = onClickListener;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_view, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setWidth(UIUtils.getScreenWidth(mContext) - UIUtils.dp2px(mContext, 30));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, view);
        //title
        tv_title.setText(TextUtils.isEmpty(title) ? "请选择" : title);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_bottom_view, textList) {

            @Override
            protected void convert(BaseViewHolder helper, T item) {
                if (item instanceof String) {
                    helper.setText(R.id.tv_text, item.toString());
                }
            }
        };
        recyclerView.setAdapter(mAdapter);
//        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mListener != null) {
                    mListener.selectItem(position);
                    dismiss();
                }
            }
        });

        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        if (textList.size() > 6) {
            lp.height = UIUtils.dp2px(mContext, 44 * 6 + 20);
        } else {
            lp.height = UIUtils.dp2px(mContext, 44 * textList.size());
        }
        recyclerView.setLayoutParams(lp);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(((Activity) mContext));
        //关闭 是键盘
        KeyBoardUtils.hideKeyBoard(parent, mContext);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        UIUtils.lightOn((Activity) mContext);
    }

    @OnClick(R.id.tv_cancle)
    void cancleClick() {
        dismiss();
    }

    public interface OnClickListener {
        void selectItem(int pos);
    }

}
