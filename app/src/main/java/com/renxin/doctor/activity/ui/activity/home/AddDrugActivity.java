package com.renxin.doctor.activity.ui.activity.home;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.OPenPaperBaseBean;
import com.renxin.doctor.activity.ui.bean_jht.DrugBean;
import com.renxin.doctor.activity.ui.bean_jht.SearchDrugBean;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.ui.presenter.OpenPaperPresenter;
import com.renxin.doctor.activity.utils.SoftHideKeyBoardUtil;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.popupwindow.OnePopupWheel;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTextChanged;

/**
 * AddDrugActivity 编辑处方药材
 * Create at 2018/4/26 下午6:15 by mayakun
 */
public class AddDrugActivity extends BaseActivity implements OpenPaperContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.search_recycleview)
    RecyclerView searchRecycleview;

    @Inject
    OpenPaperPresenter mPresenter;
    private List<SearchDrugBean> searchSearchDrugBeans = new ArrayList<>();
    private List<DrugBean> drugBeans = new ArrayList<>();
    private List<OPenPaperBaseBean.CommBean> userTypeList = new ArrayList<>();
    private List<String> userTypeListStr = new ArrayList<>();
    private BaseQuickAdapter adapterSearch, adapter;
    private OnePopupWheel mPopupWheel;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_add_drug;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        //头部处理
        initToolbar();
        //煎发
        userTypeList = getIntent().getParcelableArrayListExtra("usetype");
        if (userTypeList == null || userTypeList.isEmpty()) {
            userTypeListStr.add("常规");
        } else {
            for (OPenPaperBaseBean.CommBean commBean : userTypeList) {
                userTypeListStr.add(commBean.name);
            }
        }
        //添加的药材列表处理
        adapter = new BaseQuickAdapter<DrugBean, BaseViewHolder>(R.layout.item_add_drug, drugBeans) {
            @Override
            protected void convert(BaseViewHolder helper, DrugBean item) {
                EditText editText = helper.getView(R.id.et_num);
                if (editText.getTag() instanceof TextWatcher) {
                    editText.removeTextChangedListener((TextWatcher) editText.getTag());
                }

                helper.setText(R.id.tv_drugname, item.name)
                        .setTag(R.id.et_num, helper.getLayoutPosition())
                        .setText(R.id.et_num, item.drug_num > 0 ? (item.drug_num + "") : "")
                        .setText(R.id.tv_usertype, item.decoction)
                        .addOnClickListener(R.id.tv_drugname)
                        .addOnClickListener(R.id.tv_usertype);

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
//                        onEditTextAfterTextChanged(editable, drugBeans.indexOf(item));
                        if (TextUtils.isEmpty(editable.toString().trim())) {
                            item.drug_num = 0;
                        } else {
                            item.drug_num = Integer.parseInt(editable.toString());
                        }
                    }
                };
                editText.addTextChangedListener(textWatcher);
                editText.setTag(textWatcher);
            }
        };
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        recyclerview.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerview);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_drugname:
                        upDataDrugList(drugBeans.get(position), false);
//                        drugBeans.remove(position);
//                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.tv_usertype:
                        mPopupWheel = new OnePopupWheel(AddDrugActivity.this, userTypeListStr, "请选择煎发", new OnePopupWheel.Listener() {
                            @Override
                            public void completed(int i) {
                                drugBeans.get(position).decoction = userTypeListStr.get(i);
                                ((TextView) view).setText(userTypeListStr.get(i));
                            }
                        });
                        mPopupWheel.show(view);
                        break;
                }
            }
        });

        //搜索的药材列表处理
        adapterSearch = new BaseQuickAdapter<SearchDrugBean, BaseViewHolder>(R.layout.item_search_drug, searchSearchDrugBeans) {
            @Override
            protected void convert(BaseViewHolder helper, SearchDrugBean item) {
                helper.setText(R.id.tv_drugname, item.name)
                        .setText(R.id.tv_drugprice, item.price + "元/" + item.unit);
            }
        };
        searchRecycleview.setAdapter(adapterSearch);
        //### important! setLayoutManager should be called after setAdapter###
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        searchRecycleview.setLayoutManager(linearLayoutManager);
        adapterSearch.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchDrugBean bean = searchSearchDrugBeans.get(position);

                DrugBean drugBean = new DrugBean();
                drugBean.drug_num = 0;
                drugBean.drug_id = bean.id;
                drugBean.name = bean.name;
                drugBean.decoction = userTypeListStr.get(0);
                upDataDrugList(drugBean, true);
            }
        });
    }


    //更新药材列表
    private void upDataDrugList(DrugBean bean, boolean isAdd) {
        if (isAdd) {
            drugBeans.add(bean);
        } else {
            drugBeans.remove(bean);
        }
        adapter.notifyDataSetChanged();
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    //手机号监听
    @OnTextChanged(value = R.id.et_searchcontent, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterDrubNameChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            return;
        }
        mPresenter.searchDrugName(s.toString());
    }

    //头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("添加药材")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("保存", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        ToastUtil.showShort("保存");
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

    @Override
    public void onSuccess(Message message) {
        if (message == null || message.what != OpenPaperPresenter.SEARCH_DRUG_OK) {
            return;
        }
        searchSearchDrugBeans.clear();
        searchSearchDrugBeans.addAll((List<SearchDrugBean>) message.obj);
        adapterSearch.notifyDataSetChanged();
        searchRecycleview.scrollToPosition(0);
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

    //必须在外面保存editetext数据，不然会数据错乱
    private void onEditTextAfterTextChanged(Editable editable, int position) {
        if (TextUtils.isEmpty(editable.toString().trim())) {
            drugBeans.get(position).drug_num = 0;
        } else {
            drugBeans.get(position).drug_num = Integer.parseInt(editable.toString());
        }
    }


}
