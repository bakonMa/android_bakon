package com.renxin.doctor.activity.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.widget.recycle_view.FooterState;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    private final int NORMALLAYOUT = 0;

    private final int FOOTERLAYOUT = 1;

    public FooterHolder mFooterHolder;

    public RecyclerAdapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
    }

    public void addAll(List<String> list) {
        int lastIndex = mDatas.size();
        if (mDatas.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size())
            return FOOTERLAYOUT;
        else
            return NORMALLAYOUT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALLAYOUT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
            return new MyViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_view, parent, false);
            mFooterHolder = new FooterHolder(view);
            return mFooterHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_num);
        }

        public void setData(int pos) {
            tv.setText(mDatas.get(pos));
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        View mLoading, mNoMore;

        public FooterHolder(View itemView) {
            super(itemView);
            mLoading = itemView.findViewById(R.id.id_view_loading);
            mNoMore = itemView.findViewById(R.id.id_view_nomore);
        }

        public void setState(int state) {
            switch (state) {
                case FooterState.NORMAL:
                    hideAll();
                    break;
                case FooterState.LOADING:
                    hideAll();
                    mLoading.setVisibility(View.VISIBLE);
                    break;
                case FooterState.NOMORE:
                    hideAll();
                    mNoMore.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private void hideAll() {
            if (mLoading != null) {
                mLoading.setVisibility(View.GONE);
            }
            if (mNoMore != null) {
                mNoMore.setVisibility(View.GONE);
            }
        }

    }
}