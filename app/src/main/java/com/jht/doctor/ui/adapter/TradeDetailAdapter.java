package com.jht.doctor.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jht.doctor.R;
import com.jht.doctor.widget.recycle_view.FooterState;

import java.util.ArrayList;
import java.util.List;


public class TradeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    private final int NORMALLAYOUT = 0;

    private final int FOOTERLAYOUT = 1;

    private final int HEADERLAYOUT = 2;

    private final int FAILURELAYOUT = 3;

    public FooterHolder mFooterHolder;

    public TradeDetailAdapter(Context context) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mDatas.add(i + "");
        }
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
        if (position == mDatas.size() + 1)
            return FOOTERLAYOUT;
        else if (position == 0)
            return HEADERLAYOUT;
        else if(position ==3){
            return FAILURELAYOUT;
        }else{
            return NORMALLAYOUT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALLAYOUT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_trade_detail, parent, false);
            return new MyViewHolder(view);
        } else if(viewType == FOOTERLAYOUT){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_view, parent, false);
            mFooterHolder = new FooterHolder(view);
            return mFooterHolder;
        }else if(viewType == HEADERLAYOUT){
            view = LayoutInflater.from(mContext).inflate(R.layout.header_trade_detail, parent, false);
            return new HeaderHolder(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.item_trade_detail_failure, parent, false);
            return new FailureHolder(view);
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
        return mDatas.size() + 2;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder{

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class FailureHolder extends RecyclerView.ViewHolder{

        public FailureHolder(View itemView) {
            super(itemView);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View line;

        public MyViewHolder(View view) {
            super(view);
            line = view.findViewById(R.id.id_line);
        }

        public void setData(int pos) {
            if (pos == mDatas.size()) {
                line.setVisibility(View.GONE);
            }
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