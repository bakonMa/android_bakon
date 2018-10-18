package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.CommPaperInfoBean;
import com.junhetang.doctor.ui.bean.DrugBean;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.SearchDrugBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.widget.dialog.CommSuperDialog;
import com.junhetang.doctor.widget.dialog.SavePaperDialog;
import com.junhetang.doctor.widget.popupwindow.BottomListPopupView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * AddDrugActivity 编辑处方药材
 * Create at 2018/4/26 下午6:15 by mayakun
 */
public class AddDrugActivity extends BaseActivity implements OpenPaperContact.View {
    private final int REQUEST_CODE_SHOOSE_COMMPAPER = 2030;//选择常用处方

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.search_recycleview)
    RecyclerView searchRecycleview;
    @BindView(R.id.tv_commpaper)
    TextView tvCommpaper;
    @BindView(R.id.tv_totalmoney)
    TextView tvTotalmoney;
    @BindView(R.id.rb_good)
    RadioButton rbGood;
    @BindView(R.id.rb_comm)
    RadioButton rbComm;
    @BindView(R.id.rg_changedrug)
    RadioGroup rgChangedrug;
    @BindView(R.id.tv_drug_ps)
    TextView tvDrugPS;
    @BindView(R.id.et_searchcontent)
    EditText etSearch;

    @Inject
    OpenPaperPresenter mPresenter;
    //编辑常用处方使用
    private String title, mExplain;
    private int id;
    private int drugStoreId;//药房id

    private List<SearchDrugBean> searchSearchDrugBeans = new ArrayList<>();
    //最初的药材json，用于比较是否修改了(常用处方)
    private String commDrugJsonTemp = "";
    //最初的药材json，用于比较是否修改了(开方)
    private String addDrugJsonTemp = "";
    private ArrayList<DrugBean> drugBeans;
    private List<String> userTypeListStr = new ArrayList<>();
    private BaseQuickAdapter adapterSearch, adapter;
    private int formtype = 0;//0：添加处方 1：在线开放 2：编辑处方 3：载入处方
    private boolean hasError = false;//提交前检查是否有重复或者不可用药材
    private SavePaperDialog savePaperDialog;//保存dialog
    private ToolbarBuilder toolbarBuilder;
    private Gson gson;
    private BottomListPopupView bottomPopupView;
    private GestureDetectorCompat mDetector;//手势
    private CommSuperDialog commSuperDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_add_drug;
    }

    //煎法，sp中获取，处理数据
    private void getBaseData() {
        OPenPaperBaseBean oPenPaperBaseBean = U.getOpenpapeBaseData();
        for (OPenPaperBaseBean.CommBean commBean : oPenPaperBaseBean.drugremark) {
            userTypeListStr.add(commBean.name);
        }
        if (userTypeListStr == null || userTypeListStr.isEmpty()) {
            userTypeListStr.add("常规");
        }
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        drugBeans = getIntent().getParcelableArrayListExtra("druglist");
        if (drugBeans == null) {
            drugBeans = new ArrayList<>();
        }
        formtype = getIntent().getIntExtra("form", 0);
        id = getIntent().getIntExtra("id", 0);
        drugStoreId = getIntent().getIntExtra("store_id", 0);
        title = getIntent().getStringExtra("title");
        mExplain = getIntent().getStringExtra("m_explain");
        rgChangedrug.setVisibility((formtype == 1 || formtype == 3) ? View.VISIBLE : View.GONE);//开方显示，其他不显示
        tvDrugPS.setVisibility((formtype == 1 || formtype == 3) ? View.VISIBLE : View.GONE);//开方显示，其他不显示

        gson = new Gson();
        //比较是否改变（开方）
        addDrugJsonTemp = gson.toJson(drugBeans);
        //比较是否改变 (常用处方)
        ArrayList<CommPaperInfoBean> commbeans = getIntent().getParcelableArrayListExtra("commbean");
        conversionDrugBean(commbeans);

        //煎法 数据
        getBaseData();
        //头部处理
        initToolbar();
        //选择常用处方的时候显示，其他不显示
        tvCommpaper.setVisibility((formtype == 1 || formtype == 3) ? View.VISIBLE : View.GONE);

        //添加的药材列表处理
        adapter = new BaseQuickAdapter<DrugBean, BaseViewHolder>(R.layout.item_add_drug, drugBeans) {
            @Override
            protected void convert(BaseViewHolder helper, DrugBean item) {
                EditText editText = helper.getView(R.id.et_num);
                if (editText.getTag() instanceof TextWatcher) {
                    editText.removeTextChangedListener((TextWatcher) editText.getTag());
                }

                helper.setText(R.id.tv_drugname, item.drug_name)
                        .setTag(R.id.et_num, helper.getLayoutPosition())
                        .setText(R.id.et_num, item.drug_num > 0 ? (item.drug_num + "") : "")
                        .setText(R.id.tv_usertype, item.decoction)
                        .setText(R.id.tv_unit, item.unit)
                        .addOnClickListener(R.id.iv_del)
                        .addOnClickListener(R.id.tv_usertype);

                if (TextUtils.isEmpty(item.spec) || item.unit.equals(item.spec)) {
                    helper.setGone(R.id.tv_drugspec, false);
                } else {
                    helper.setGone(R.id.tv_drugspec, true)
                            .setText(R.id.tv_drugspec, item.spec);
                }

                //是否可用或者重复状态
                boolean isError = (item.use_flag == 0 || isHasInList(helper.getLayoutPosition()));
                if (isError) {//发现有相同的就修改
                    hasError = isError;
                }
                helper.setTextColor(R.id.tv_drugname, UIUtils.getColor(isError ? R.color.red : R.color.color_000))
                        .setTextColor(R.id.et_num, UIUtils.getColor(isError ? R.color.red : R.color.color_000))
                        .setTextColor(R.id.tv_unit, UIUtils.getColor(isError ? R.color.red : R.color.color_000))
                        .setTextColor(R.id.tv_usertype, UIUtils.getColor(isError ? R.color.red : R.color.color_000));

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
                        //更新价格
                        updataTotalMoney();
                    }
                };
                editText.addTextChangedListener(textWatcher);
                editText.setTag(textWatcher);
                //焦点问题
                if (helper.getLayoutPosition() == drugBeans.size() - 1) {
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                } else {
                    editText.clearFocus();
                }
            }
        };
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        recyclerview.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerview);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_del:
                        if (position < drugBeans.size()) {//快速删除的时候 会有bug
                            upDataDrugList(drugBeans.get(position), false);
                        }
                        break;
                    case R.id.tv_usertype:
                        //中成药、西药、器械“用法”不可选
                        if (!"ZY".equals(drugBeans.get(position).drug_type)) {
                            return;
                        }

                        bottomPopupView = new BottomListPopupView(AddDrugActivity.this, "请选择用法", userTypeListStr, new BottomListPopupView.OnClickListener() {
                            @Override
                            public void selectItem(int i) {
                                drugBeans.get(position).decoction = userTypeListStr.get(i);
                                ((TextView) view).setText(userTypeListStr.get(i));
                            }
                        });
                        bottomPopupView.show(scrollView);

                        break;
                }
            }
        });
        if (drugBeans != null && !drugBeans.isEmpty()) {
            //更新价格
            updataTotalMoney();
        }

        //搜索的药材列表处理
        adapterSearch = new BaseQuickAdapter<SearchDrugBean, BaseViewHolder>(R.layout.item_search_drug, searchSearchDrugBeans) {
            @Override
            protected void convert(BaseViewHolder helper, SearchDrugBean item) {
                helper.setText(R.id.tv_drugname, item.name);
                if (item.datatype == 1) {//药物
                    if (TextUtils.isEmpty(item.spec) || item.unit.equals(item.spec)) {
                        helper.setText(R.id.tv_drugprice, item.price + "元/" + item.unit);
                    } else {
                        helper.setText(R.id.tv_drugprice, String.format("%s元/%s(%s)", item.price, item.unit, item.spec));
                    }
                } else {//处方
                    helper.setText(R.id.tv_drugprice, TextUtils.isEmpty(item.type_title) ? "" : item.type_title);
                }
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
                //数据类型，1:药品，2：处方
                if (bean.datatype == 1) {
                    DrugBean drugBean = new DrugBean();
                    drugBean.drug_num = 0;
                    drugBean.drug_id = bean.id;
                    drugBean.mcode = bean.mcode;
                    drugBean.drug_type = bean.drug_type;
                    drugBean.use_flag = bean.use_flag;
                    drugBean.drug_name = bean.name;
                    drugBean.unit = bean.unit;
                    drugBean.spec = bean.spec;
                    drugBean.price = bean.price;
                    drugBean.sub_drug_type = bean.sub_drug_type;
                    drugBean.decoction = userTypeListStr.get(0);
                    upDataDrugList(drugBean, true);
                } else {
                    //获取处方信息
                    mPresenter.searchDrugPaperById(drugStoreId, bean.id, bean.datatype);
                }
                etSearch.setText("");
            }
        });

        //手势监听，滑动关闭软键盘
        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动距离超过**像素就收起键盘
                if (Math.abs(distanceY) > 5) {
                    KeyBoardUtils.hideKeyBoard(etSearch, actContext());
                }
                return Math.abs(distanceY) > 5;
            }
        });

        //添加手势监听
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    //当前list中是否已经存在相同id的药材
    private boolean isHasInList(int pos) {
        boolean isOneMore = false;
        for (int i = 0; i < drugBeans.size(); i++) {
            if (i == pos) {
                continue;//跳过自己的pos的循环，继续下面的
            }
            //使用所有药房都一样的唯一的code来判定是否有相同的药材
            if (drugBeans.get(pos).mcode.equals(drugBeans.get(i).mcode)) {
                isOneMore = true;
                break;//循环结束
            }
        }
        LogUtil.d("isHasInList=" + isOneMore);
        return isOneMore;
    }

    //更新药材总价格
    private String totalMoney;

    private void updataTotalMoney() {
        double money = 0d;
        for (DrugBean bean : drugBeans) {
            money += bean.price * bean.drug_num;
        }
        totalMoney = String.format("%.2f", money);
        tvTotalmoney.setText(String.format("预计：%s元", totalMoney));
    }

    //更新药材列表
    private void upDataDrugList(DrugBean bean, boolean isAdd) {
        if (isAdd) {
            drugBeans.add(bean);
        } else {
            drugBeans.remove(bean);
        }
        //修改全局状态
        hasError = false;
        adapter.notifyDataSetChanged();
        //滑动到底部
        if (!drugBeans.isEmpty()) {
            recyclerview.scrollToPosition(drugBeans.size() - 1);
        }
        //更新价格
        updataTotalMoney();
    }

    //输入搜索监听
    @OnTextChanged(value = R.id.et_searchcontent, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterDrubNameChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            return;
        }
        mPresenter.searchDrugName(drugStoreId, s.toString());
    }

    @OnClick({R.id.rb_comm, R.id.rb_good, R.id.tv_cleanall, R.id.tv_commpaper})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rb_comm:
                drugTypeCheange(2);
                break;
            case R.id.rb_good:
                drugTypeCheange(1);
                break;
            case R.id.tv_cleanall:
                if (drugBeans.isEmpty()) {
                    return;
                }
                drugBeans.clear();
                //修改全局状态
                hasError = false;
                adapter.notifyDataSetChanged();
                tvTotalmoney.setText("预计：0元");
                break;
            case R.id.tv_commpaper://常用处方
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.online_commpaper);
                Intent intent = new Intent(this, ChooseCommActivity.class);
//                intent.putExtra("form", 1);//进入选择方子
                intent.putExtra("store_id", drugStoreId);//药房id
                startActivityForResult(intent, REQUEST_CODE_SHOOSE_COMMPAPER);
                break;
        }
    }

    //普药 精品药转换
    private void drugTypeCheange(int type) {
        if (drugBeans == null || drugBeans.isEmpty()) {
            ToastUtil.showShort("请先添加药材");
            return;
        }
        mPresenter.changeDrugType(drugStoreId, type, gson.toJson(drugBeans));
    }

    //头部处理
    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("编辑药材")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("保存", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        //点击保存
                        clickSave();
                    }
                }).bind();
        //编辑状态，显示title
        if (formtype == 2 && !TextUtils.isEmpty(title)) {
            toolbarBuilder.setTitle(title);
        }
    }

    /**
     * 点击返回
     */
    @Override
    public void onBackPressed() {
        if (formtype == 1 || formtype == 3) {//开方
            if ((TextUtils.isEmpty(addDrugJsonTemp) && drugBeans.isEmpty())
                    || addDrugJsonTemp.equals(gson.toJson(drugBeans))) {//空列表 或者么有改变 直接返回
                finish();
                return;
            } else {
                commSuperDialog = new CommSuperDialog(this, "您尚未保存是否退出？", new CommSuperDialog.ClickListener() {
                    @Override
                    public void btnOnClick(int btnId) {
                        if (btnId == R.id.btn_right) {
                            finish();
                        }
                    }
                });
                commSuperDialog.show();
            }
        } else {// 常用处方 返回提醒保存
            //列表完全一样 直接关闭
            if ((TextUtils.isEmpty(commDrugJsonTemp) && drugBeans.isEmpty())
                    || commDrugJsonTemp.equals(gson.toJson(drugBeans))) {
                finish();
                return;
            }
            commSuperDialog = new CommSuperDialog(this, "您尚未保存是否退出？", new CommSuperDialog.ClickListener() {
                @Override
                public void btnOnClick(int btnId) {
                    if (btnId == R.id.btn_right) {
                        finish();
                    }
                }
            });
            commSuperDialog.show();
        }
    }

    private List<String> typeList = new ArrayList<>();

    //点击保存
    private void clickSave() {
        if (drugBeans.isEmpty()) {
            showCommSuperDialog("请添加药材");
            return;
        }
        //是否有重复药材
        if (hasError) {
            showCommSuperDialog("处方中有重复药材或者此药房药材不足");
            return;
        }
        //药材用量是否填写
        for (DrugBean bean : drugBeans) {
            if (bean.drug_num <= 0) {
                showCommSuperDialog("请填写全部药材的用量");
                return;
            }
        }

        if (formtype == 1 || formtype == 3) {//返回 在线开放
            typeList.clear();
            //药材用量
            for (DrugBean bean : drugBeans) {
                if (!typeList.contains(bean.drug_type)) {
                    typeList.add(bean.drug_type);
                }
            }
            if (typeList.size() == 1) {
                switch (formtype) {
                    case 1://返回开方
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("druglist", drugBeans);
                        intent.putExtra("drug_type", typeList.get(0));//药材类型
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case 3://载入处方 返回开方
                        HashMap dataMap = new HashMap();
                        dataMap.put("drug_type", typeList.get(0));//药材类型
                        dataMap.put("store_id", drugStoreId);//药房id
                        dataMap.put("druglist", drugBeans);//药房id
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_JZR_EDITE_DRUG, dataMap));
                        finish();
                        break;
                }

            } else {
                //中草药，西药。。拼接
                StringBuffer typeStr = new StringBuffer();
                for (String s : typeList) {
                    if (!TextUtils.isEmpty(typeStr)) {
                        typeStr.append("、");
                    }
                    typeStr.append(Constant.DRUG_TYPE.get(s));
                }

                showCommSuperDialog(String.format(getString(R.string.str_drug_type), typeStr.toString()));
            }

        } else {
            savePaperDialog = new SavePaperDialog(this, new SavePaperDialog.ClickListener() {
                @Override
                public void confirm(String name) {
                    if(null == name){//取消
                        savePaperDialog.dismiss();
                        return;
                    }
                    Params params = new Params();
                    if (formtype == 2) {
                        params.put("id", id);
                    }
                    params.put("title", name);
                    params.put("param", gson.toJson(drugBeans));
                    mPresenter.addOftenmed(params);
                }
            });
            savePaperDialog.show();
            //编辑状态 显示原来的值
            if (formtype == 2) {
                savePaperDialog.setEditeText(title, mExplain);
            }
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case OpenPaperPresenter.SEARCH_DRUG_OK://搜索药材列表
                searchSearchDrugBeans.clear();
                searchSearchDrugBeans.addAll((List<SearchDrugBean>) message.obj);
                adapterSearch.notifyDataSetChanged();
                searchRecycleview.scrollToPosition(0);
                break;
            case OpenPaperPresenter.GET_COMMPAPER_INFO_OK://药材中的常用处方数据
                List<CommPaperInfoBean> paperInfoBeans = (List<CommPaperInfoBean>) message.obj;
                for (CommPaperInfoBean bean : paperInfoBeans) {
                    DrugBean tempDrugBean = new DrugBean();
                    tempDrugBean.drug_id = bean.id;
                    tempDrugBean.mcode = bean.mcode;
                    tempDrugBean.drug_type = bean.drug_type;
                    tempDrugBean.drug_name = bean.name;
                    tempDrugBean.unit = bean.unit;
                    tempDrugBean.spec = bean.spec;
                    tempDrugBean.price = bean.price;
                    tempDrugBean.decoction = bean.decoction;
                    tempDrugBean.use_flag = bean.use_flag;
                    tempDrugBean.drug_num = bean.drug_num;

                    drugBeans.add(tempDrugBean);
                }
                //修改全局状态
                hasError = false;
                adapter.notifyDataSetChanged();
                //滑动到底部
                if (!drugBeans.isEmpty()) {
                    recyclerview.scrollToPosition(drugBeans.size() - 1);
                }
                //更新价格
                updataTotalMoney();
                break;
            case OpenPaperPresenter.ADD_COMMPAPER_OK:
                //添加常用处方成功
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_ADD_COMMMEPAPER));
                finish();
                break;
            case OpenPaperPresenter.CHANGE_DRUG_OK:
                //普药 精品相互转成功
                List<DrugBean> newBeans = (List<DrugBean>) message.obj;
                if (newBeans != null && !newBeans.isEmpty()) {
                    drugBeans.clear();
                    drugBeans.addAll(newBeans);
                    adapter.notifyDataSetChanged();
                    //更新价格
                    updataTotalMoney();
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        showCommSuperDialog(errorMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHOOSE_COMM_PAPER://选择常用处方 回调
                ArrayList<CommPaperInfoBean> commbeans = (ArrayList<CommPaperInfoBean>) event.getData();
                conversionDrugBean(commbeans);
                //修改全局状态
                hasError = false;
                adapter.notifyDataSetChanged();
                //更新价格
                updataTotalMoney();
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SHOOSE_COMMPAPER) {//选择常用处方 回调
            if (null == data) {
                return;
            }
            ArrayList<CommPaperInfoBean> commbeans = data.getParcelableArrayListExtra("commbean");
            conversionDrugBean(commbeans);
            //修改全局状态
            hasError = false;
            adapter.notifyDataSetChanged();
            //更新价格
            updataTotalMoney();
        }
    }

    //把常用处方数据，转化为drugbean
    private void conversionDrugBean(ArrayList<CommPaperInfoBean> commbeans) {
        if (commbeans == null || commbeans.isEmpty()) {
            return;
        }
        for (CommPaperInfoBean bean : commbeans) {
            DrugBean tempBean = new DrugBean();
            tempBean.drug_id = bean.id;
            tempBean.mcode = bean.mcode;
            tempBean.drug_type = bean.drug_type;
            tempBean.drug_name = bean.name;
            tempBean.unit = bean.unit;
            tempBean.spec = bean.spec;
            tempBean.price = bean.price;
            tempBean.decoction = bean.decoction;
            tempBean.use_flag = bean.use_flag;
            tempBean.drug_num = bean.drug_num;
            drugBeans.add(tempBean);
        }
        //临时列表，为了比较是否修改了list
        commDrugJsonTemp = gson.toJson(drugBeans);
    }

    //确定 dialog
    private void showCommSuperDialog(@NotNull String msg) {
        commSuperDialog = new CommSuperDialog(this, msg);
        commSuperDialog.show();
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

    @Override
    public void onPause() {
        super.onPause();
        //关闭键盘
        KeyBoardUtils.hideKeyBoard(etSearch, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (savePaperDialog != null) {
            savePaperDialog.dismiss();
            savePaperDialog = null;
        }
        if (commSuperDialog != null) {
            commSuperDialog.dismiss();
            commSuperDialog = null;
        }
    }
}
