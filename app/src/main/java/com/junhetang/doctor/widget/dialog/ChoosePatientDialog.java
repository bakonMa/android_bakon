package com.junhetang.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.utils.UIUtils;

import java.util.List;


/**
 * ChoosePatientDialog 根据手机号 查询所有就诊人
 * Create at 2018/8/29 下午2:07 by mayakun
 */

public class ChoosePatientDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private TextView tv_write;
    private RecyclerView recyclerView;

    private OnClickListener listener;
    private BaseQuickAdapter mAdapter;
    private List<PatientFamilyBean.JiuzhenBean> patientBeans;

    public ChoosePatientDialog(@NonNull Activity context, List<PatientFamilyBean.JiuzhenBean> patientBeans, OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.patientBeans = patientBeans;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_choosepatient, null);
        tv_write = view.findViewById(R.id.tv_write);
        recyclerView = view.findViewById(R.id.recycler_patient);
        tv_write.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<PatientFamilyBean.JiuzhenBean, BaseViewHolder>(R.layout.item_choose_patient_dialog, patientBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PatientFamilyBean.JiuzhenBean item) {
                helper.setText(R.id.tv_name, TextUtils.isEmpty(item.patient_name) ? "" : item.patient_name)
                        .setText(R.id.tv_age, item.age + "岁")
//                        .setText(R.id.tv_relation, Constant.RELATION_TYPE[item.relationship])
                        .setText(R.id.tv_sex, item.sex == 0 ? "男" : "女");
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) {
                    listener.selectItem(patientBeans.get(position));
                }
                dismiss();
            }
        });
        recyclerView.setAdapter(mAdapter);

        //限制高度
        if (patientBeans.size() > 5) {
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = UIUtils.dp2px(mContext, 40 * 5);//最大显示5条数据的高度
            recyclerView.setLayoutParams(layoutParams);
        }

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
        setContentView(view);
        setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    //选择 监听回调
    public interface OnClickListener {
        void selectItem(PatientFamilyBean.JiuzhenBean bean);
    }

}
