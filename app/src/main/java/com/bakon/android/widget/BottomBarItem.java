package com.bakon.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.utils.UIUtils;

import java.util.Locale;


/**
 * @author ChayChan
 * @description: 底部tab条目
 * @date 2017/6/23  9:14
 */

public class BottomBarItem extends LinearLayout {

    private Context mContext;
    private String mText;//文本
    private int mTextSize = 12;//文字大小 默认为12sp
    private int mMarginTop = 0;//文字和图标的距离,默认0dp

    private int mTextColorNormal = 0xFF999999;    //描述文本的默认显示颜色
    private int mTextColorSelected = 0xFF46C01B;  //述文本的默认选中显示颜色
    private Drawable mIconDrawable;//图标的selector文件
    private int mIconWidth;//图标的宽度
    private int mIconHeight;//图标的高度
    private int mItemPadding;//BottomBarItem的padding


    private ImageView mImageView;
    private TextView mTvUnread;
    private TextView mTvRedPoint;
    private TextView mTextView;

    private int mUnreadTextSize = 10; //未读数默认字体大小10sp
    private int mMsgTextSize = 6; //消息默认字体大小6sp
    private int unreadNumThreshold = 99;//大于99 显示99+

    public BottomBarItem(Context context) {
        this(context, null);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);

        //icon，text，testsize
        mIconDrawable = ta.getDrawable(R.styleable.BottomBarItem_iconDrawable);
        mText = ta.getString(R.styleable.BottomBarItem_itemText);
        mTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, UIUtils.sp2px(mContext, mTextSize));
        mTextColorNormal = ta.getColor(R.styleable.BottomBarItem_textColorNormal, mTextColorNormal);
        mTextColorSelected = ta.getColor(R.styleable.BottomBarItem_textColorSelected, mTextColorSelected);
        //icon 大小
        mIconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth, 0);
        mIconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight, 0);
        mItemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemPadding, 0);
        //设置未读数字体大小
        mUnreadTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_unreadTextSize, UIUtils.sp2px(mContext, mUnreadTextSize));
        //设置提示消息字体大小
        mMsgTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_msgTextSize, UIUtils.sp2px(mContext, mMsgTextSize));
        //设置未读数组阈值 大于阈值的数字将显示为 n+ n为设置的阈值
        unreadNumThreshold = ta.getInteger(R.styleable.BottomBarItem_unreadThreshold, 99);

        ta.recycle();

        checkValues();
        init();
    }

    /**
     * 检查传入的值是否完善
     */
    private void checkValues() {
        if (mIconDrawable == null) {
            throw new IllegalStateException("没有指定iconDrawable");
        }
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = View.inflate(mContext, R.layout.item_bottom_bar, null);
        if (mItemPadding != 0) {
            //如果有设置item的padding
            view.setPadding(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
        }
        mImageView = (ImageView) view.findViewById(R.id.iv_icon);
        mTvUnread = (TextView) view.findViewById(R.id.tv_num);
        mTvRedPoint = (TextView) view.findViewById(R.id.tv_redpoint);
        mTextView = (TextView) view.findViewById(R.id.tv_title);

        mImageView.setImageDrawable(mIconDrawable);

        if (mIconWidth != 0 && mIconHeight != 0) {
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            FrameLayout.LayoutParams imageLayoutParams = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
            imageLayoutParams.width = mIconWidth;
            imageLayoutParams.height = mIconHeight;
            mImageView.setLayoutParams(imageLayoutParams);
        }

        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);//设置底部文字字体大小
        mTvUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnreadTextSize);//设置未读数的字体大小

        mTextView.setTextColor(mTextColorNormal);//设置底部文字字体颜色
        mTextView.setText(mText);//设置标签文字

        addView(view);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }


    //修改选择状态
    public void setStatus(boolean isSelected) {
        mImageView.setSelected(isSelected);
        mTextView.setTextColor(isSelected ? mTextColorSelected : mTextColorNormal);
    }


    //阀值
    public int getUnreadNumThreshold() {
        return unreadNumThreshold;
    }

    public void setUnreadNumThreshold(int unreadNumThreshold) {
        this.unreadNumThreshold = unreadNumThreshold;
    }

    /**
     * 设置未读数
     *
     * @param unreadNum 小于等于{unreadNumThreshold}则隐藏，
     *                  大于0小于{unreadNumThreshold}则显示对应数字，
     *                  超过{unreadNumThreshold} 显示 {unreadNumThreshold}+
     */
    public void setUnreadNum(int unreadNum) {
        if (unreadNum <= 0) {
            mTvUnread.setVisibility(GONE);
        } else if (unreadNum <= unreadNumThreshold) {
            mTvUnread.setVisibility(VISIBLE);
            mTvRedPoint.setVisibility(GONE);
            mTvUnread.setText(String.valueOf(unreadNum));
        } else {
            mTvUnread.setVisibility(VISIBLE);
            mTvRedPoint.setVisibility(GONE);
            mTvUnread.setText(String.format(Locale.CHINA, "%d+", unreadNumThreshold));
        }
    }

    //红点
    public void showNotify() {
        //红点显示
        mTvRedPoint.setVisibility(VISIBLE);
        //数字隐藏
        mTvUnread.setVisibility(GONE);
    }

    public void hideNotify() {
        mTvRedPoint.setVisibility(GONE);
    }
}
