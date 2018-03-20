package com.jht.doctor.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.ui.bean.BankCardBean;

/**
 * Created by Tang on 2017/11/16.
 */

public class BankCardSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int NORMALLAYOUT = 0;

    private final int HEADERLAYOUT = 1;

    private BankCardBean bankCardBean;

    public BankCardSettingAdapter(Context mContext, BankCardBean bankCardBean) {
        this.mContext = mContext;
        this.bankCardBean = bankCardBean;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADERLAYOUT;
        else
            return NORMALLAYOUT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADERLAYOUT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.header_bankcard_setting, null);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bankcard_setting, null);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setData(position);
        }
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).setData();
        }
    }

    @Override
    public int getItemCount() {
        return bankCardBean.getJoint().size() + (TextUtils.isEmpty(bankCardBean.getOwner().getBankNo()) ? 0 : 1);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_head;
        View view_line;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_line = itemView.findViewById(R.id.id_line);
        }

        public void setData(int position) {
            if (position != 1) {
                tv_head.setVisibility(View.GONE);
            }
            if (position == 5) {
                view_line.setVisibility(View.GONE);
            }
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_bank, btn_unbind;

        public HeaderHolder(View itemView) {
            super(itemView);

        }

        public void setData() {

        }
    }
}
