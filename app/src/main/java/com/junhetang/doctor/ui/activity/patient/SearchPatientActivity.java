package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.PatientBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * SearchPatientActivity 搜索患者
 * Create at 2018/8/31 上午10:32 by mayakun
 */
public class SearchPatientActivity extends BaseActivity implements PatientContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recyvleview;
    @BindView(R.id.et_serch)
    EditText etSerch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    @Inject
    PatientPresenter mPresenter;

    private List<PatientBean> patientBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private String searchStr = "";

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_choose_jzr;
    }

    @Override
    protected void initView() {
        initToolbar();

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                mPresenter.searchPatient(etSerch.getText().toString().trim());
            }
        });

        recyvleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<PatientBean, BaseViewHolder>(R.layout.item_patient, patientBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PatientBean item) {
                String name = TextUtils.isEmpty(item.remark_name) ? item.nick_name : item.remark_name;
                helper.setText(R.id.tv_name, TextUtils.isEmpty(name) ? "" : UIUtils.setKeyWordColor(name, etSerch.getText().toString().trim()))
                        .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : UIUtils.setKeyWordColor(item.phone, etSerch.getText().toString().trim()))
                        .setText(R.id.tv_from, TextUtils.isEmpty(item.valid_name) ? "" : item.valid_name)
                        .setGone(R.id.tv_phone, true);

                helper.setTextColor(R.id.tv_from, UIUtils.getColor(item.is_valid == 1 ? R.color.color_30ad37 : R.color.color_main));
                UIUtils.setCompoundDrawable(helper.getView(R.id.tv_from), 12, 5,
                        (item.is_valid == 1 ? R.drawable.icon_patient_wx : R.drawable.icon_patient_paper), Gravity.LEFT);

                ImageUtil.showCircleImage(item.head_url, helper.getView(R.id.iv_headerimg));
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                Intent intent = new Intent(actContext(), PatientCenterActivity.class);
                intent.putExtra("memb_no", patientBeans.get(position).memb_no);
                intent.putExtra("im_accid", patientBeans.get(position).im_accid);
                startActivity(intent);
            }
        });
        recyvleview.setAdapter(mAdapter);

        etSerch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    deSearch();
                    return true;
                }
                return false;
            }
        });
        //弹出软键盘
        etSerch.requestFocus();

        //滚动监听器的onScrollStateChanged() 看是否在滚动。
        recyvleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {//不是空闲状态，就隐藏键盘
                    KeyBoardUtils.hideKeyBoard(etSerch, actContext());
                }
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("搜索患者")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {//返回
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    //搜索监听
    @OnTextChanged(value = R.id.et_serch, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSearchTextChanged(Editable s) {
        ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        if (s.toString().trim().length() > 0) {
            deSearch();
        }
    }

    @OnClick({R.id.iv_clear, R.id.tv_search})
    public void cleanClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etSerch.setText("");
                break;
            case R.id.tv_search:
                deSearch();
                break;
        }
    }

    private void deSearch() {
        if (TextUtils.isEmpty(etSerch.getText().toString().trim())) {
            ToastUtil.showShort("请输入患者姓名或联系电话");
            return;
        }
        if (!searchStr.equals(etSerch.getText().toString().trim())) {
            searchStr = etSerch.getText().toString().trim();
            mPresenter.searchPatient(searchStr);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.SEARCH_PATIENT_OK:
                List<PatientBean> beans = (List<PatientBean>) message.obj;
                if (beans != null) {
                    patientBeans.clear();
                    if (!beans.isEmpty()) {
                        patientBeans.addAll(beans);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (patientBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view, recyvleview);
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }


}
