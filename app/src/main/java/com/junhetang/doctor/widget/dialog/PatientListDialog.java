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
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.JobSchedulePatientBean;
import com.junhetang.doctor.utils.UIUtils;

import java.util.List;


/**
 * PatientListDialog  预约、取消 患者列表（坐诊信息）
 * Create at 2018/6/26 下午2:34 by mayakun
 */

public class PatientListDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private ImageView iv_close;
    private TextView tv_title;
    private RecyclerView recyclerView;

    private BaseQuickAdapter mAdapter;
    private int type;//1：已预约 -1：已取消
    private List<JobSchedulePatientBean> patientBeans;

    public PatientListDialog(@NonNull Activity context, int type, List<JobSchedulePatientBean> patientBeans) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.type = type;
        this.patientBeans = patientBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_patient_list, null);
        iv_close = view.findViewById(R.id.iv_close);
        tv_title = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.recycler_patient);
        tv_title.setText(type == 1 ? "(已预约)患者目录" : "(已取消)患者目录");
        tv_title.setTextColor(UIUtils.getColor(type == 1 ? R.color.color_main : R.color.blue));
        iv_close.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseQuickAdapter<JobSchedulePatientBean, BaseViewHolder>(R.layout.item_job_schedule_patient, patientBeans) {
            @Override
            protected void convert(BaseViewHolder helper, JobSchedulePatientBean item) {
                helper.setText(R.id.tv_name, TextUtils.isEmpty(item.patient_name) ? "" : item.patient_name)
                        .setText(R.id.tv_age, TextUtils.isEmpty(item.age) ? "" : item.age)
                        .setText(R.id.tv_sex, TextUtils.isEmpty(item.sex) ? "" : item.sex);
            }
        };
        mAdapter.addHeaderView(getLayoutInflater().inflate(R.layout.item_job_schedule_patient, (ViewGroup) recyclerView.getParent(), false));
        recyclerView.setAdapter(mAdapter);

        //限制高度
        if (patientBeans.size() > 7) {
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = UIUtils.dp2px(mContext, 35 * 8);//最大显示8条数据
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


}
