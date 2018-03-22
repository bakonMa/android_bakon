package com.jht.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.ui.bean.IfBankOfJointBean;
import com.jht.doctor.ui.bean.WrapperBean;
import com.jht.doctor.utils.DensityUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tang on 2017/11/9.
 */

public class SelectMultiBankJointDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private Button btn_no, btn_yes;

    private ImageView iv_close;

    private RecyclerView recyclerView;

    private List<IfBankOfJointBean.DataBean> mData;

    private List<WrapperBean<IfBankOfJointBean.DataBean>> wrapperBeans;

    private BaseQuickAdapter mAdapter;

    private int pos = 0;

    public SelectMultiBankJointDialog(@NonNull Activity context, List<IfBankOfJointBean.DataBean> bankcards, ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.mData = bankcards;
        this.mListener = clickListener;
        wrapperBeans = wrapperBeans(mData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_bankcard_multi, null);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        iv_close = view.findViewById(R.id.id_iv_close);
        iv_close.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.id_recycleView);
        setHeight();
        setAdapter();
        setContentView(view);
        setCancelable(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置adapter
     */
    private void setAdapter() {
        mAdapter = new BaseQuickAdapter(R.layout.item_select_bankcard, wrapperBeans) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                WrapperBean<IfBankOfJointBean.DataBean> wrapperBean = (WrapperBean<IfBankOfJointBean.DataBean>) item;
                String bankNo = wrapperBean.getT().getBankCardNo();
                helper.setVisible(R.id.id_line, helper.getLayoutPosition() != wrapperBeans.size() - 1)
                        .setChecked(R.id.id_cb, wrapperBean.isChecked())
                        .setText(R.id.id_tv_bank_card, MessageFormat.format("{0}(尾号{1})",
                                wrapperBean.getT().getBankName(), bankNo.substring(bankNo.length() - 4,
                                        bankNo.length())));
                CheckBox checkBox = helper.getView(R.id.id_cb);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            int current = helper.getAdapterPosition();
                            if (pos != -1) {
                                wrapperBeans.get(pos).setChecked(false);
                            }
                            wrapperBeans.get(current).setChecked(true);
                            specialUpdate(current);
                        } else {
                            if (pos == helper.getAdapterPosition()) {
                                pos = -1;
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void specialUpdate(int current) {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mAdapter.notifyItemChanged(current);
                mAdapter.notifyItemChanged(pos);
                pos = current;
            }
        };
        handler.post(r);
    }

    /**
     * 根据银行卡条数动态设置recycleview的高度
     */

    private void setHeight() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        if (mData.size() > 5) {
            lp.height = DensityUtils.dp2Px(mContext, 5 * 40);
            recyclerView.setLayoutParams(lp);
        } else {
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            recyclerView.setLayoutParams(lp);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_no:
                dismiss();
                //todo 新增银行卡
                if(mListener!=null){
                    mListener.addClicked();
                }
                break;
            case R.id.btn_ok:
                dismiss();
                //todo 确认使用
                if (mListener != null && pos != -1){
                    mListener.confirmClicked(mData.get(pos));
                }else{
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请选择您要使用的银行卡");
                }
                break;
            case R.id.id_iv_close:
                dismiss();
                break;
        }
    }

    public interface ClickListener {
        void confirmClicked(IfBankOfJointBean.DataBean dataBean);

        void addClicked();
    }

    private ClickListener mListener;

    /**
     * 包装银行卡list
     *
     * @param strs
     * @return
     */
    private List<WrapperBean<IfBankOfJointBean.DataBean>> wrapperBeans(List<IfBankOfJointBean.DataBean> strs) {
        List<WrapperBean<IfBankOfJointBean.DataBean>> wrapperBeans = new ArrayList<>();
        for (IfBankOfJointBean.DataBean dataBean : strs) {
            wrapperBeans.add(new WrapperBean<IfBankOfJointBean.DataBean>(dataBean));
        }
        wrapperBeans.get(0).setChecked(true);
        return wrapperBeans;
    }

}
