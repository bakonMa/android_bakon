package com.renxin.doctor.activity.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean_jht.BaseConfigBean;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.ui.presenter.OpenPaperPresenter;
import com.renxin.doctor.activity.utils.KeyBoardUtils;
import com.renxin.doctor.activity.utils.StatusBarUtil;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * SearchSkillNameActivity 搜索疾病名称
 * Create at 2018/4/26 下午3:41 by mayakun
 */
public class SearchSkillNameActivity extends BaseActivity implements OpenPaperContact.View {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.et_searchcontent)
    EditText etSearchcontent;

    @Inject
    OpenPaperPresenter mPresenter;
    private List<BaseConfigBean.Skill> showBeans = new ArrayList<>();
    private BaseQuickAdapter adapter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_search_skillname;
    }

    @Override
    protected void initView() {
        //头部处理
        StatusBarUtil.setStatusBarColor(this, findViewById(R.id.id_view_statu), R.color.white);
        etSearchcontent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etSearchcontent.getText().toString().trim().length() < 2) {
                        ToastUtil.showShort("请输入至少2个字符");
                    } else {
                        KeyBoardUtils.hideKeyBoard(etSearchcontent, actContext());
                        mPresenter.searchSkillName(etSearchcontent.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });

        adapter = new BaseQuickAdapter<BaseConfigBean.Skill, BaseViewHolder>(
                R.layout.item_search_skill_name, showBeans) {
            @Override
            protected void convert(BaseViewHolder helper, BaseConfigBean.Skill item) {
                helper.setText(R.id.tv_skillname, item.name);
            }
        };

        recyclerview.setAdapter(adapter);
        //### important! setLayoutManager should be called after setAdapter###
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("drug_name", showBeans.get(position).name);
                intent.putExtra("icd10_code", showBeans.get(position).icd10_code);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @OnTextChanged(value = R.id.et_searchcontent, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterContentChanged(Editable s) {
        if (s.length() < 2) {
            return;
        }
        //自动搜索
        mPresenter.searchSkillName(etSearchcontent.getText().toString().trim());
    }


    @OnClick({R.id.tv_cancle})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
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
        if (message == null || message.what != OpenPaperPresenter.SEARCH_SKILL_OK) {
            return;
        }
        showBeans.clear();
        List<BaseConfigBean.Skill> tempList = (List<BaseConfigBean.Skill>) message.obj;
        if (tempList == null || tempList.isEmpty()) {
            ToastUtil.showShort("没有查询到相关疾病");
        } else {
            showBeans.addAll(tempList);
        }
        adapter.notifyDataSetChanged();
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
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

}
