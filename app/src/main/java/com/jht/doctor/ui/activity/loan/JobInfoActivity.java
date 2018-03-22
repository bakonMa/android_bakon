package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyInfoBean;
import com.jht.doctor.ui.bean.ConfigBean;
import com.jht.doctor.ui.contact.JobInfoContact;
import com.jht.doctor.ui.presenter.JobInfoPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.ScreenUtils;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.ScheduleView;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobInfoActivity extends BaseAppCompatActivity implements JobInfoContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_ed_company_name)
    EditableLayout idEdCompanyName;
    @BindView(R.id.id_ed_company_nature)
    EditableLayout idEdCompanyNature;
    @BindView(R.id.id_ed_job_industry)
    EditableLayout idEdJobIndustry;
    @BindView(R.id.id_ed_job_rank)
    EditableLayout idEdJobRank;
    @BindView(R.id.id_ed_month_income)
    EditableLayout idEdMonthIncome;
    @BindView(R.id.id_btn_next_step)
    TextView idBtnNextStep;
    @BindView(R.id.id_activity_job_info)
    LinearLayout idActivityJobInfo;
    @BindView(R.id.id_step)
    ScheduleView idStep;

    @Inject
    JobInfoPresenter mPresenter;

    private OnePopupWheel mPopupWheel;

    private List<ConfigBean.ConfigItem> companytypeBeanList;//公司性质

    private List<ConfigBean.ConfigItem> industryBeanList; //公司行业

    private List<ConfigBean.ConfigItem> positionlevelBeanList;//职位级别

    private String codeCompanyType, codeIndustry, codePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);
        ButterKnife.bind(this);
        initToolbar();
        initEvent();
        requestData();
    }

    private void requestData() {
//        mPresenter.requestInfo();
        companytypeBeanList = U.getConfigData().COMPANY_TYPE;
        industryBeanList = U.getConfigData().INDUSTRY;
        positionlevelBeanList = U.getConfigData().POSITION_LEVEL;
    }

    private void initEvent() {
        idEdCompanyName.setMaxLength(30);
        idEdCompanyName.getEditText().addTextChangedListener(textWatcherImp);
        idEdMonthIncome.setMaxLength(8);
        idEdMonthIncome.getEditText().addTextChangedListener(textWatcherImp);
        idEdCompanyNature.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdJobIndustry.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdJobRank.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdMonthIncome.getEditText().setOnFocusChangeListener(focusChangeListenerImp);
        idEdMonthIncome.getEditText().setOnTouchListener(touchListenerImp);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("工作信息")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private View.OnTouchListener touchListenerImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdMonthIncome.getEditText().getText().toString().length() == 0) {
                        idEdMonthIncome.showUnit("元");
                        idEdMonthIncome.getEditText().setText("");
                        KeyBoardUtils.showKeyBoard(idEdMonthIncome.getEditText(), JobInfoActivity.this);
                    }
                    break;
            }
            return false;
        }
    };

    private View.OnFocusChangeListener focusChangeListenerImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdMonthIncome.getEditText().getText().toString().trim().length() == 0) {
                idEdMonthIncome.getEditText().setText("");
                idEdMonthIncome.hideUnit();
            }
        }
    };

    private TextWatcher textWatcherImp = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            idBtnNextStep.setEnabled(idEdCompanyName.getEditText().getText().toString().length() > 0
                    && idEdMonthIncome.getEditText().getText().toString().trim().length() > 0
                    && idEdCompanyNature.getText().length() > 0
                    && idEdJobIndustry.getText().length() > 0
                    && idEdJobRank.getText().length() > 0);
        }
    };

    @OnClick({R.id.id_ed_company_nature, R.id.id_ed_job_industry, R.id.id_ed_job_rank, R.id.id_btn_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_ed_company_nature:
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(companytypeBeanList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        codeCompanyType = companytypeBeanList.get(position).itemVal;
                        idEdCompanyNature.setText(companytypeBeanList.get(position).colNameCn);
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_job_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_ed_job_industry:
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(industryBeanList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        codeIndustry = industryBeanList.get(position).itemVal;
                        idEdJobIndustry.setText(industryBeanList.get(position).colNameCn);
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_job_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_ed_job_rank:
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(positionlevelBeanList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        codePosition = positionlevelBeanList.get(position).itemVal;
                        idEdJobRank.setText(positionlevelBeanList.get(position).colNameCn);
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_job_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_btn_next_step:
                startActivity(new Intent(actContext(), HouseInfoActivity.class));
//                mPresenter.commitJobInfo(idEdCompanyName.getText().trim()
//                        , codeCompanyType
//                        , codeIndustry
//                        , codePosition
//                        , Double.parseDouble(idEdMonthIncome.getText().trim()));
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case JobInfoPresenter.COMMIT_JOB_INFO:
                startActivity(new Intent(this, HouseInfoActivity.class));
                break;
            case JobInfoPresenter.APPLY_INFO:
                EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_APPLY_INFO_BASIC));
                ApplyInfoBean applyInfoBean = (ApplyInfoBean) message.obj;
                showInfo(applyInfoBean);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_APPLY_INFO_JOB:
                    requestData();
                    break;
                case EventConfig.FINISH_LOAN:
                    finish();
                    break;
            }
        }

    }

    /**
     * 展示已提交的信息
     *
     * @param applyInfoBean
     */
    private void showInfo(ApplyInfoBean applyInfoBean) {
        if (applyInfoBean == null) {
            return;
        }
        idStep.setCurrentIndex(U.checkStep(applyInfoBean));
        ApplyInfoBean.UserJobDTOBean userJobDTOBean = applyInfoBean.getUserJobDTO();
        if (userJobDTOBean != null) {
            idEdCompanyName.getEditText().setText(userJobDTOBean.getCompanyName());
            codeCompanyType = userJobDTOBean.getCompanyType();
            idEdCompanyNature.setText(U.keyToValue(codeCompanyType, companytypeBeanList));
            codeIndustry = userJobDTOBean.getIndustry();
            idEdJobIndustry.setText(U.keyToValue(codeIndustry, industryBeanList));
            codePosition = userJobDTOBean.getPosition();
            idEdJobRank.setText(U.keyToValue(codePosition, positionlevelBeanList));
            idEdMonthIncome.getEditText().setText((int) userJobDTOBean.getMonthlyIncome() + "");
            idEdMonthIncome.showUnit("元");
        }

    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }
}
