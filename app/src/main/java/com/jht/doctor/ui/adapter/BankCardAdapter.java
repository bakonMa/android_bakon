package com.jht.doctor.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jht.doctor.R;
import com.jht.doctor.ui.bean.BankCardBean;
import com.jht.doctor.utils.RegexUtil;


/**
 * Created by Tang on 2017/11/16.
 */

public class BankCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int NORMALLAYOUT = 0;

    private final int HEADERLAYOUT = 1;

    private BankCardBean bankCardBean;

    public BankCardAdapter(Context mContext, BankCardBean bankCardBean, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.mContext = mContext;
        this.bankCardBean = bankCardBean;
    }

    public void setData(BankCardBean bankCardBean) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.header_bank_card, parent, false);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setData(position);
        } else {
            ((HeaderHolder) holder).setData();
        }
    }

    @Override
    public int getItemCount() {
        return (bankCardBean.getJoint() == null ? 0 : bankCardBean.getJoint().size()) + 2;
    }

    /**
     * 共借人
     */
    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_phone, tv_identification;

        LinearLayout ll_top;//共借人头信息

        RelativeLayout rl_add_people;//添加共借人按钮

        RelativeLayout rl_added;//已添加局部

        LinearLayout ll_info;//已添加上方信息 分两种情况 不需要银行 和需要银行卡

        LinearLayout ll_bank_added;//下方银行卡（已添加）

        RelativeLayout rl_bank_add;//下方银行卡(未添加)

        TextView tv_bank_name, tv_bank_no;

        ImageView id_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.id_tv_name);
            tv_phone = itemView.findViewById(R.id.id_tv_phone);
            tv_identification = itemView.findViewById(R.id.id_tv_identification);
            ll_top = itemView.findViewById(R.id.id_ll_top);
            rl_add_people = itemView.findViewById(R.id.id_rl_add_people);
            rl_added = itemView.findViewById(R.id.id_rl_added);
            ll_info = itemView.findViewById(R.id.id_ll_info_top);
            ll_bank_added = itemView.findViewById(R.id.id_ll_bank_added);
            rl_bank_add = itemView.findViewById(R.id.id_rl_bank_add);
            tv_bank_name = itemView.findViewById(R.id.id_tv_bankname);
            tv_bank_no = itemView.findViewById(R.id.id_tv_bankCard);
            id_img = itemView.findViewById(R.id.id_img);
        }

        public void setData(int position) {
            if (position != 1) {
                //头部信息只显示一次
                ll_top.setVisibility(View.GONE);
            }
            if (position != bankCardBean.getJoint().size() + 1) {
                //共借人列表
                BankCardBean.JointBean jointBean = bankCardBean.getJoint().get(position - 1);
                rl_add_people.setVisibility(View.GONE);
                rl_added.setVisibility(View.VISIBLE);

                tv_name.setText(RegexUtil.hideFirstName(jointBean.getUserName()));
                tv_phone.setText(RegexUtil.hidePhone(jointBean.getUserPhone()));
                tv_identification.setText(RegexUtil.hideIDNormal(jointBean.getUserIdCard()));
                switch (jointBean.getIsBank()) {
                    case "03":
                        //需要绑卡
                        ll_info.setVisibility(View.VISIBLE);
                        ll_info.setBackgroundResource(R.drawable.bank_top);
                        if (TextUtils.isEmpty(jointBean.getBankNo())) {
                            //未绑卡
                            ll_bank_added.setVisibility(View.GONE);
                            rl_bank_add.setVisibility(View.VISIBLE);
                            rl_bank_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (clickListener != null) {
                                        clickListener.addCoborrowerCard(position - 1);
                                    }
                                }
                            });
                        } else {
                            //绑过卡
                            ll_bank_added.setVisibility(View.VISIBLE);
                            rl_bank_add.setVisibility(View.GONE);
                            Glide.with(mContext).load(jointBean.getBankLogoUrl()).into(id_img);
                            tv_bank_name.setText(jointBean.getBankName());
                            String bankNo = jointBean.getBankNo();
                            tv_bank_no.setText(bankNo.substring(bankNo.length() - 4, bankNo.length()));
                        }
                        break;
                    case "02":
                    case "01":
                        //不需要绑卡
                        ll_info.setVisibility(View.VISIBLE);
                        ll_info.setBackgroundResource(R.drawable.bank_top_info_only);
                        ll_bank_added.setVisibility(View.GONE);
                        rl_bank_add.setVisibility(View.GONE);
                        break;
                }
            } else {
                //最下方永远有一个添加共借人按钮
                rl_add_people.setVisibility(View.VISIBLE);
                rl_added.setVisibility(View.GONE);
                rl_add_people.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_rl_add_people:
                    //添加共借人
                    if (clickListener != null) {
                        clickListener.addCoborrower();
                    }
                    break;
            }
        }


    }

    /**
     * 主借人
     */
    private class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_phone, tv_identification, tv_bank_name, tv_bank_no;

        LinearLayout ll_added;

        RelativeLayout rl_add;

        ImageView id_img;

        public HeaderHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.id_tv_name);
            tv_phone = itemView.findViewById(R.id.id_tv_phone);
            tv_identification = itemView.findViewById(R.id.id_tv_identification);
            ll_added = itemView.findViewById(R.id.id_ll_added);
            rl_add = itemView.findViewById(R.id.id_rl_add);
            tv_bank_name = itemView.findViewById(R.id.id_bank_name);
            tv_bank_no = itemView.findViewById(R.id.id_bank_no);
            id_img = itemView.findViewById(R.id.id_img);
        }

        public void setData() {
            tv_name.setText(RegexUtil.hideFirstName(bankCardBean.getOwner().getUserName()));
            tv_phone.setText(RegexUtil.hidePhone(bankCardBean.getOwner().getUserPhone()));
            tv_identification.setText(RegexUtil.hideIDNormal(bankCardBean.getOwner().getUserIdCard()));
            if (TextUtils.isEmpty(bankCardBean.getOwner().getBankNo())) {
                //未添加银行卡
                ll_added.setVisibility(View.GONE);
                rl_add.setVisibility(View.VISIBLE);
                rl_add.setOnClickListener(this);
            } else {
                //已添加银行卡
                ll_added.setVisibility(View.VISIBLE);
                rl_add.setVisibility(View.GONE);
                Glide.with(mContext).load(bankCardBean.getOwner().getBankLogoUrl()).into(id_img);
                tv_bank_name.setText(bankCardBean.getOwner().getBankName());
                String bankNo = bankCardBean.getOwner().getBankNo();
                tv_bank_no.setText(bankNo.substring(bankNo.length() - 4, bankNo.length()));
            }
        }

        @Override
        public void onClick(View view) {
            //添加主借人银行卡
            if (clickListener != null) {
                clickListener.addMainCard();
            }
        }
    }


    private ClickListener clickListener;

    public interface ClickListener {
        void addMainCard();

        void addCoborrower();

        void addCoborrowerCard(int pos);
    }
}
