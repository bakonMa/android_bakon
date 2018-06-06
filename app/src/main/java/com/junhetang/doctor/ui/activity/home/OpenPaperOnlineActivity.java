package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.nim.NimU;
import com.junhetang.doctor.ui.activity.patient.PatientFamilyActivity;
import com.junhetang.doctor.ui.activity.patient.PatientListActivity;
import com.junhetang.doctor.ui.adapter.OPenPaperDrugAdapter;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.DrugBean;
import com.junhetang.doctor.ui.bean.JiuZhenHistoryBean;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.OnlinePaperBackBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.SavePaperDialog;
import com.junhetang.doctor.widget.popupwindow.OnePopupWheel;
import com.junhetang.doctor.widget.popupwindow.TwoPopupWheel;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * OpenPaperOnlineActivity 在线开方
 * Create at 2018/4/25 下午4:23 by mayakun
 */
public class OpenPaperOnlineActivity extends BaseActivity implements OpenPaperContact.View {

    public static final int REQUEST_CODE_DOCADVICE = 1020;
    public static final int REQUEST_CODE_SEARCHSKILLNAME = 1021;
    public static final int REQUEST_CODE_ADDDRUG = 1022;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_addpatient)
    TextView tvAddpatient;
    @BindView(R.id.tv_editepatient)
    TextView tv_editepatient;
    @BindView(R.id.llt_jzinfo)
    LinearLayout lltJZinfo;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditTextlayout etAge;
    @BindView(R.id.et_phone)
    EditTextlayout etPhone;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_usetype)
    EditableLayout etUsetype;
    @BindView(R.id.et_docadvice)
    EditableLayout etDocadvice;
    @BindView(R.id.et_drugstore)
    EditableLayout etDrugstore;
    @BindView(R.id.et_drugclass)
    EditableLayout etDrugClass;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_daijian)
    RadioGroup rgDaijian;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_serverprice)
    EditText etServerprice;
    @BindView(R.id.tv_skillname)
    TextView tvSkillname;
    @BindView(R.id.tv_showall)
    TextView tvShowall;
    @BindView(R.id.tv_choose_history)
    TextView tvChooseHistory;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;

    @Inject
    OpenPaperPresenter mPresenter;

    private int formParent = 0;//是否来自聊天(0 默认不是 1：聊天)
    private int storeId = -1;//药房id
    private int drugClassId;//剂型id，
    private String usagesStr = "";//用法str
    private String freqStr = "";//用量str
    private int sexType = 0;
    private int daijianType = 0;
    private String membNo = "";//患者编号，选择患者才有
    private int relationship = 4;//就诊人关系（不是选择 默认4-其他）
    private String pAccid = "";//患者云信 accid
    private String docadviceStr = "";//医嘱
    private ArrayList<DrugBean> drugBeans = new ArrayList<>();
    private boolean isShowAll = false;//展开全部

    private OPenPaperBaseBean baseBean;
    private List<String> drugStoreList = new ArrayList<>();//药房
    private List<String> drugClassList = new ArrayList<>();//剂型
    private List<String> drugUseList = new ArrayList<>();//用法
    private List<String> frequencyList = new ArrayList<>();//用量
    private OnePopupWheel mPopupWheel;
    private OPenPaperDrugAdapter adapter;
    private SavePaperDialog savePaperDialog;

    //带有返回的startActivityForResult-仅限nim中使用 formParent=1
    public static void startResultActivity(Context context, int requestCode, int formParent, String p_accid, String membNo) {
        Intent intent = new Intent(context, OpenPaperOnlineActivity.class);
        intent.putExtra("formParent", formParent);
        intent.putExtra("memb_no", membNo);
        intent.putExtra("p_accid", p_accid);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_openpaper_online;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        //来源
        formParent = getIntent().getIntExtra("formParent", 0);
        membNo = getIntent().getStringExtra("memb_no");//患者momb_no
        pAccid = getIntent().getStringExtra("p_accid");//患者accid

        //初始基础数据
        initBaseData();
        //设置topbar
        initToolbar();
        //聊天进来不能填写
        tv_editepatient.setVisibility(formParent == 0 ? View.VISIBLE : View.GONE);
        //性别
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });
        //代煎
        rgDaijian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                daijianType = (i == R.id.rb_yes ? 0 : 1);
            }
        });

        //添加的药材列表处理
        adapter = new OPenPaperDrugAdapter(this, drugBeans, 3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//        adapter.bindToRecyclerView(recyclerview);
    }

    //初始base数据
    private void initBaseData() {
        baseBean = U.getOpenpapeBaseData();
        //药房
        for (OPenPaperBaseBean.StoreBean bean : baseBean.store) {
            drugStoreList.add(bean.drug_store_name);
        }
        //剂型
        for (OPenPaperBaseBean.CommBean bean : baseBean.drug_class) {
            drugClassList.add(bean.name);
        }
        //用法
        for (OPenPaperBaseBean.CommBean bean : baseBean.usage) {
            drugUseList.add(bean.name);
        }
        //用量
        for (OPenPaperBaseBean.CommBean bean : baseBean.frequency) {
            frequencyList.add(bean.name);
        }
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("在线开方")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
                    }

                }).bind();
    }

    @OnClick({R.id.tv_addpatient, R.id.tv_editepatient, R.id.et_drugstore, R.id.et_drugclass,
            R.id.tv_adddrug, R.id.tv_minus_one, R.id.tv_add_one, R.id.et_usetype, R.id.tv_showall,
            R.id.tv_addcommpaper, R.id.tv_skillname, R.id.et_docadvice, R.id.tv_choose_history, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addpatient://选择患者
                Intent intent = new Intent();
                if (formParent == 1) {
                    intent.setClass(this, PatientFamilyActivity.class);
                    intent.putExtra("memb_no", membNo);
                } else {
                    intent.setClass(this, PatientListActivity.class);
                }
                intent.putExtra("formtype", 1);//来自 选择患者
                startActivity(intent);
                break;
            case R.id.tv_editepatient://编辑就诊人
                writeJzInfo();
                break;
            case R.id.tv_choose_history://选择历史就诊人
                startActivity(new Intent(this, JiuZhenHistoryActivity.class));
                break;
            case R.id.tv_addcommpaper://添加为常用处方
                if (drugBeans == null || drugBeans.isEmpty()) {
                    ToastUtil.showShort("请添加药材");
                    return;
                }
                savePaperDialog = new SavePaperDialog(this, new SavePaperDialog.ClickListener() {
                    @Override
                    public void confirm(String name, String remark) {
                        Params params = new Params();
                        params.put("title", name);
                        params.put("m_explain", remark);
                        params.put("param", new Gson().toJson(drugBeans));
                        mPresenter.addOftenmed(params);
                        savePaperDialog.dismiss();
                    }
                });
                savePaperDialog.show();
                break;
            case R.id.tv_showall://展开
                setOPenStatus(!isShowAll);
                break;
            case R.id.et_drugstore://药房
                //是否选过了药房 切换药房 要提醒
                if (TextUtils.isEmpty(etDrugstore.getText())) {
                    chooseDrugStore();
                } else {
                    commonDialog = new CommonDialog(this, false, "切换药房会导致药品信息变更，\n是否确定切换药房？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == R.id.btn_ok) {
                                chooseDrugStore();
                            }
                        }
                    });
                    commonDialog.show();
                }
                break;
            case R.id.et_drugclass://剂型
                mPopupWheel = new OnePopupWheel(this, drugClassList, "请选择剂型", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        drugClassId = baseBean.drug_class.get(position).id;
                        etDrugClass.setText(drugClassList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.et_usetype://用法用量
                TwoPopupWheel mTwoWheel = new TwoPopupWheel(this, "请选择用法用量",
                        drugUseList, frequencyList, new TwoPopupWheel.ClickedListener() {
                    @Override
                    public void completeClicked(int pos1, int pos2) {
                        usagesStr = drugUseList.get(pos1);
                        freqStr = frequencyList.get(pos2);
                        etUsetype.setText(usagesStr + "-" + freqStr);
                    }
                });
                mTwoWheel.show(scrollView);
                break;
            case R.id.tv_minus_one://副减1
                editeDrugNum(false);
                break;
            case R.id.tv_add_one://副加1
                editeDrugNum(true);
                break;
            case R.id.tv_skillname://疾病名称
                Intent intentSkillname = new Intent(this, SearchSkillNameActivity.class);
                startActivityForResult(intentSkillname, REQUEST_CODE_SEARCHSKILLNAME);
                break;
            case R.id.et_docadvice://医嘱
                Intent intentDocAdvice = new Intent(this, ChooseDocAdviceActivity.class);
                intentDocAdvice.putParcelableArrayListExtra("beanlist", baseBean.docadvice);
                intentDocAdvice.putExtra("docadvice", docadviceStr);
                startActivityForResult(intentDocAdvice, REQUEST_CODE_DOCADVICE);
                break;
            case R.id.tv_adddrug://添加药材
                //是否选择了药房
                if (TextUtils.isEmpty(etDrugstore.getText())) {
                    commonDialog = new CommonDialog(this, "请先选择药房");
                    commonDialog.show();
                    return;
                }

                Intent addDrugIntent = new Intent(this, AddDrugActivity.class);
                addDrugIntent.putParcelableArrayListExtra("druglist", drugBeans);
                addDrugIntent.putExtra("form", 1);//添加药材使用
                addDrugIntent.putExtra("store_id", storeId);//药房id
                startActivityForResult(addDrugIntent, REQUEST_CODE_ADDDRUG);
                break;
            case R.id.tv_next_step://提交
                checkData();
                break;
        }
    }

    //打开选择药房
    private void chooseDrugStore() {
        mPopupWheel = new OnePopupWheel(this, drugStoreList, "请选择药房", new OnePopupWheel.Listener() {
            @Override
            public void completed(int position) {
                storeId = baseBean.store.get(position).drug_store_id;
                etDrugstore.setText(drugStoreList.get(position));
            }
        });
        mPopupWheel.show(scrollView);
    }

    //展开折叠
    private void setOPenStatus(boolean b) {
        isShowAll = b;
        if (drugBeans.isEmpty()) {
            tvShowall.setVisibility(View.GONE);
        } else {
            tvShowall.setVisibility(View.VISIBLE);
            adapter.setIsShowAll(isShowAll);
            tvShowall.setSelected(isShowAll);
            tvShowall.setText(isShowAll ? "收起" : "展开");
        }
    }

    //要副加减
    private void editeDrugNum(boolean isAdd) {
        int currNum = Integer.parseInt(etNum.getText().toString());
        if (isAdd) {
            etNum.setText(currNum + 1 + "");
        } else {
            etNum.setText(currNum > 1 ? currNum - 1 + "" : "1");
        }
    }

    //选择的患者的就诊人（第一类）
    private void chooseJzInfo(PatientFamilyBean.JiuzhenBean bean) {
        lltJZinfo.setVisibility(View.VISIBLE);
        tvChooseHistory.setVisibility(View.GONE);
        etName.setEditeText(TextUtils.isEmpty(bean.patient_name) ? "" : bean.patient_name);
        etPhone.setEditeText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
        membNo = bean.id;
        relationship = bean.relationship;
        pAccid = bean.getIm_accid();//记录需要
        etName.setEditeEnable(false);
        etAge.setEditeEnable(false);
        etPhone.setEditeEnable(false);
        rbNan.setEnabled(false);
        rbNv.setEnabled(false);
    }

    //手写就诊人信息
    private void writeJzInfo() {
        lltJZinfo.setVisibility(View.VISIBLE);
        tvChooseHistory.setVisibility(View.VISIBLE);
        etName.setEditeEnable(true);
        etAge.setEditeEnable(true);
        etPhone.setEditeEnable(true);
        rbNan.setEnabled(true);
        rbNv.setEnabled(true);
        relationship = 4;//关系 其他
        membNo = "";
        pAccid = "";
        etName.setEditeText("");
        etAge.setEditeText("");
        etPhone.setEditeText("");
        rgSex.check(R.id.rb_nan);
    }

    //选择填写的历史就诊人（第二类）
    private void chooseJzHistoryInfo(JiuZhenHistoryBean bean) {
        etName.setEditeText(TextUtils.isEmpty(bean.patient_name) ? "" : bean.patient_name);
        etPhone.setEditeText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
        relationship = 4;//关系 其他
        membNo = "";
        pAccid = "";
        //不可修改
        etName.setEditeEnable(false);
        etAge.setEditeEnable(false);
        etPhone.setEditeEnable(false);
        rbNan.setEnabled(false);
        rbNv.setEnabled(false);
    }

    //数据检测
    private void checkData() {
        if (TextUtils.isEmpty(etName.getEditText().getText())
                || TextUtils.isEmpty(etAge.getEditText().getText())) {
            commonDialog = new CommonDialog(this, "请填写就诊人信息");
            commonDialog.show();
            return;
        }
        //主述 可以不填写
//        if (TextUtils.isEmpty(skilNameCode)) {
//            commonDialog = new CommonDialog(this, "请选择主述及辩证型");
//            commonDialog.show();
//            return;
//        }
        if (TextUtils.isEmpty(etDrugstore.getText())) {
            commonDialog = new CommonDialog(this, "请选择药房");
            commonDialog.show();
            return;
        }
        if (TextUtils.isEmpty(etDrugClass.getText())) {
            commonDialog = new CommonDialog(this, "请选择剂型");
            commonDialog.show();
            return;
        }
        if (drugBeans == null || drugBeans.isEmpty()) {
            commonDialog = new CommonDialog(this, "请添加药材");
            commonDialog.show();
            return;
        }
        if (TextUtils.isEmpty(etUsetype.getText())) {
            commonDialog = new CommonDialog(this, "请选择用法用量");
            commonDialog.show();
            return;
        }
        if (TextUtils.isEmpty(docadviceStr)) {
            commonDialog = new CommonDialog(this, "请填写医嘱");
            commonDialog.show();
            return;
        }

        Params params = new Params();
        //患者编号
        if (!TextUtils.isEmpty(membNo)) {
            params.put("memb_no", membNo);
        }
        params.put("relationship", relationship);
        params.put("source", formParent == 0 ? 1 : 2);//来源：1：首页，2：聊天
        params.put("name", etName.getEditText().getText());
        params.put("sex", sexType);
        params.put("age", etAge.getEditText().getText());
        params.put("phone", etPhone.getEditText().getText());
        if (!TextUtils.isEmpty(skilNameCode)) {
            params.put("icd10", skilNameCode);//主述及辩证型的icd10_code字段
        }
        params.put("store_id", storeId);
        params.put("param", new Gson().toJson(drugBeans));
        params.put("drug_class", drugClassId);
        params.put("boiled_type", daijianType);
        params.put("drug_num", Integer.parseInt(etNum.getText().toString()));
        params.put("usages", usagesStr);
        params.put("freq", freqStr);
        //补充收费
        if (!TextUtils.isEmpty(etServerprice.getText().toString().trim())) {
            params.put("service_price", etServerprice.getText().toString().trim());
        }
        params.put("doc_remark", docadviceStr);//医嘱
        mPresenter.openPaperOnline(params);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private CommonDialog commonDialog;

    @Override
    public void onError(String errorCode, String errorMsg) {
        commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }


    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case OpenPaperPresenter.OPENPAPER_CAMERA_OK://开方ok
                if (formParent == 1) {//聊天开方
                    setResult(RESULT_OK, new Intent());
                    finish();
                } else {//普通开方
                    commonDialog = new CommonDialog(this, true, "处方已上传至药房", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    commonDialog.show();
                }
                break;
            case OpenPaperPresenter.ADD_COMMPAPER_OK:
                commonDialog = new CommonDialog(this, "添加常用处方成功");
                commonDialog.show();
                break;
            case OpenPaperPresenter.OPENPAPER_ONLINE_OK://提交后
                OnlinePaperBackBean bean = (OnlinePaperBackBean) message.obj;
                if (bean == null) {//data为空  说明提交成功
                    //可以拿到paccid 就记录，没有就不记录
                    if (!TextUtils.isEmpty(pAccid)) {
                        mPresenter.addChatRecord(NimU.getNimAccount(), pAccid, Constant.CHAT_RECORD_TYPE_3, formParent);
                    }

                    if (formParent == 1) {//聊天开方
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {//普通开方
                        commonDialog = new CommonDialog(this, true, "处方已上传至药房", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        commonDialog.show();
                    }
                } else {//药物和药房 有不匹配，处理返回的数据
                    //status为-4时返回处方中不能用的药品param，其他状态未定义
                    if (bean.status == -4) {
                        drugBeans.clear();
                        drugBeans.addAll(bean.param);
                        adapter.notifyDataSetChanged();
                    }
                    commonDialog = new CommonDialog(this, "药房缺少药材，请修改处方");
                    commonDialog.show();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHOOSE_PATIENT://选择就诊人
                PatientFamilyBean.JiuzhenBean bean = (PatientFamilyBean.JiuzhenBean) event.getData();
                if (bean != null) {
                    chooseJzInfo(bean);
                }
                break;
            case EventConfig.EVENT_KEY_CHOOSE_JIUZHEN_HISTORY://选择历史就诊人
                JiuZhenHistoryBean jiuZhenHistoryBean = (JiuZhenHistoryBean) event.getData();
                if (jiuZhenHistoryBean != null) {
                    chooseJzHistoryInfo(jiuZhenHistoryBean);
                }
                break;
        }
    }

    //主述及辩证型
    private String skilNameCode = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_ADDDRUG://添加药材
                drugBeans.clear();
                drugBeans.addAll(data.getParcelableArrayListExtra("druglist"));
                adapter.notifyDataSetChanged();
                //默认折叠
                setOPenStatus(false);
                break;
            case REQUEST_CODE_DOCADVICE://医嘱
                docadviceStr = data.getStringExtra("docadvice");
                etDocadvice.setText(docadviceStr);
                break;
            case REQUEST_CODE_SEARCHSKILLNAME://搜索疾病名称 主述及辩证型
                String skilName = data.getStringExtra("drug_name");
                if (TextUtils.isEmpty(skilName)) {
                    skilNameCode = "";
                    tvSkillname.setTextColor(UIUtils.getColor(R.color.color_999));
                    tvSkillname.setText("");
                } else {
                    skilNameCode = data.getStringExtra("icd10_code");
                    tvSkillname.setTextColor(UIUtils.getColor(R.color.color_000));
                    tvSkillname.setText(skilName);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        commonDialog = new CommonDialog(this, false, "确定要退出开方吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_ok) {
                    finish();
                }
            }
        });
        commonDialog.show();
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
