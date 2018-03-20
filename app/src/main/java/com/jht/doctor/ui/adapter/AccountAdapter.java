package com.jht.doctor.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.ui.activity.repayment.RechageActivity;
import com.jht.doctor.ui.activity.repayment.TradeDetailActivity;
import com.jht.doctor.ui.activity.repayment.WithdrawCashActivity;
import com.jht.doctor.widget.recycle_view.FooterState;

import java.util.ArrayList;
import java.util.List;


public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    private final int NORMALLAYOUT = 0;

    private final int FOOTERLAYOUT = 1;

    private final int HEADERLAYOUT = 2;

    public FooterHolder mFooterHolder;

    public AccountAdapter(Context context) {
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
        else
            return NORMALLAYOUT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALLAYOUT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_my_account, parent, false);
            return new MyViewHolder(view);
        } else if(viewType == FOOTERLAYOUT){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_view, parent, false);
            mFooterHolder = new FooterHolder(view);
            return mFooterHolder;
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.item_my_account_header, parent, false);
            return new HeaderHolder(view);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View line;
        TextView btn_detail;
        TextView btn_recharge,btn_get_money;

        public MyViewHolder(View view) {
            super(view);
            line = view.findViewById(R.id.id_line);
            btn_detail = view.findViewById(R.id.id_btn_detail);
            btn_recharge = view.findViewById(R.id.id_btn_recharge);
            btn_get_money = view.findViewById(R.id.id_btn_get_money);
        }

        public void setData(int pos) {
            if (pos == mDatas.size()) {
                line.setVisibility(View.GONE);
            }
            btn_detail.setOnClickListener(this);
            btn_recharge.setOnClickListener(this);
            btn_get_money.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.id_btn_detail:
                    mContext.startActivity(new Intent(mContext, TradeDetailActivity.class));
                    break;
                case R.id.id_btn_recharge:
                    mContext.startActivity(new Intent(mContext, RechageActivity.class));
                    break;
                case R.id.id_btn_get_money:
                    mContext.startActivity(new Intent(mContext, WithdrawCashActivity.class));
                    break;
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