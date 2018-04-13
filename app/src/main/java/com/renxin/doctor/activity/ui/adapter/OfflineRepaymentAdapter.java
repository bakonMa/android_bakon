package com.renxin.doctor.activity.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renxin.doctor.activity.R;


/**
 * Created by Tang on 2017/11/16.
 */

public class OfflineRepaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int NORMALLAYOUT = 0;

    private final int HEADERLAYOUT = 1;

    public OfflineRepaymentAdapter(Context mContext) {
        this.mContext = mContext;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.header_offline_repayment, null);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_offline_repayment, null);
            return new MyViewHolder(view);
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
        return 10 + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        View line_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            line_view = itemView.findViewById(R.id.id_line);
        }

        public void setData(int position) {
            if (position == 10) {
                line_view.setVisibility(View.GONE);
            }
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
