package com.renxin.doctor.activity.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.widget.recycle_view.FooterState;

import java.util.List;


public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    private final int NORMALLAYOUT = 0;

    private final int FOOTERLAYOUT = 1;

    public FooterHolder mFooterHolder;

    public CommonAdapter(Context context, List<String> data) {
        mDatas = data;
        mContext = context;
    }

    /*@Override
    public int getItemViewType(int position) {
        if (position == mDatas.size())
            return FOOTERLAYOUT;
        else
            return NORMALLAYOUT;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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