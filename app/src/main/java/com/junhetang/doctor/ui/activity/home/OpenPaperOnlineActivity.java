package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.nim.NimU;
import com.junhetang.doctor.ui.activity.patient.PatientFamilyActivity;
import com.junhetang.doctor.ui.adapter.JzrPopupAdapter;
import com.junhetang.doctor.ui.adapter.OPenPaperDrugAdapter;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.DrugBean;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.OnlinePaperBackBean;
import com.junhetang.doctor.ui.bean.PaperInfoBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.RegexUtil;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.SavePaperDialog;
import com.junhetang.doctor.widget.popupwindow.BottomListPopupView;
import com.junhetang.doctor.widget.popupwindow.TwoPopupWheel;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * OpenPaperOnlineActivity 在线开方
 * Create at 2018/4/25 下午4:23 by mayakun
 */
public class OpenPaperOnlineActivity extends BaseActivity implements OpenPaperContact.View {

    public static final int REQUEST_CODE_DOCADVICE = 1020;
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
    AutoCompleteTextView etName;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditTextlayout etAge;
    @BindView(R.id.et_phone)
    EditText etPhone;
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
    @BindView(R.id.tv_addcommpaper)
    TextView tvAddcommpaper;
    @BindView(R.id.et_memb_see)
    EditableLayout etMembSee;
    @BindView(R.id.rlt_daijian)
    RelativeLayout rltDaijian;
    @BindView(R.id.rg_daijian)
    RadioGroup rgDaijian;
    @BindView(R.id.rlt_num)
    RelativeLayout rltNum;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_serverprice)
    EditText etServerprice;
    @BindView(R.id.et_skillname)
    EditText etSkillname;
    @BindView(R.id.tv_drug_info)
    TextView tvDrugInfo;
    @BindView(R.id.tv_money_service)
    TextView tvMoneyService;
    @BindView(R.id.tv_money_drug)
    TextView tvMoneyDrug;
    @BindView(R.id.tv_money_total)
    TextView tvMoneyTotal;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;

    @Inject
    OpenPaperPresenter mPresenter;

    private int formParent = 0;//是否来自聊天(0 默认不是 1：聊天)
    private int storeId = -1;//药房id
    private int drugClassId = -1;//剂型id，
    private String usagesStr = "";//用法str
    private String freqStr = "";//用量str
    private int sexType = 0;
    private int daijianType = 1;
    private int membSee = 0;//购药前患者是否可见，默认0 不可见
    private String membNo = "";//患者编号，选择患者才有
    private int relationship = 4;//就诊人关系（不是选择 默认4-其他）
    private String pAccid = "";//患者云信 accid
    private String docadviceStr = "";//医嘱
    private String drugType = "";//处方药材类型 “ZY”：”中草药” “ZCY”：”中成药” “XY” ：”西药” “QC” ：”器材”
    private ArrayList<DrugBean> drugBeans = new ArrayList<>();
    private int checekId;//处方id，【调用此放】使用
    private OPenPaperBaseBean baseBean;
    private List<String> drugStoreList = new ArrayList<>();//药房
    private List<String> drugClassList = new ArrayList<>();//剂型
    private List<String> menbSeeList = new ArrayList<>();//是否可见
    private List<String> drugUseList = new ArrayList<>();//用法
    private List<String> frequencyList = new ArrayList<>();//用量
    private OPenPaperDrugAdapter adapter;
    private SavePaperDialog savePaperDialog;
    private GestureDetectorCompat mDetector;//手势
    private BottomListPopupView bottomPopupView;
    private boolean isChoosePatient = false;//是否点击的选择就诊人
    private List<PatientFamilyBean.JiuzhenBean> jzrList = new ArrayList<>();//手机号的就诊人

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
        checekId = getIntent().getIntExtra("checkid", 0);//处方id，【调用此放】使用

        //初始基础数据
        initBaseData();
        //设置topbar
        initToolbar();
        //聊天进来不能填写
        tv_editepatient.setVisibility(formParent == 0 ? View.VISIBLE : View.GONE);
        //默认 不可见
        etMembSee.setText(menbSeeList.get(membSee));
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
                if (i == R.id.rb_yes) {//代煎，必须是中药材，汤剂副数>5可代煎，其他（膏，散，丸）必须代煎（隐藏）
                    //小于5副 不可代煎
                    if (TextUtils.isEmpty(etNum.getText().toString().trim()) || Integer.parseInt(etNum.getText().toString()) < 5) {
                        daijianType = 1;
                        rgDaijian.check(R.id.rb_no);
                        commonDialog = new CommonDialog(OpenPaperOnlineActivity.this, "5副及以上才可选择代煎");
                        commonDialog.show();
                        return;
                    } else {
                        daijianType = 0;
                    }
                } else {
                    daijianType = 1;
                }
            }
        });
        //添加的药材列表处理
        adapter = new OPenPaperDrugAdapter(this, drugBeans);
        recyclerView.setAdapter(adapter);
        //### important! setLayoutManager should be called after setAdapter###
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //判断是【调用此方】
        if (checekId > 0) {
            mPresenter.getPaperInfo(checekId);
        }
        //聊天开方，是否有未完成的处方，可以继续开方
        if (formParent == 1 && !TextUtils.isEmpty(membNo)) {
            String jsonTemp = DocApplication.getAppComponent().dataRepo().appSP().getString(membNo, "");
            if (!TextUtils.isEmpty(jsonTemp)) {
                commonDialog = new CommonDialog(this, false, "此患者有未结束的处方，是否继续使用此处方？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btn_ok) {
                            editePaperInfoDate(new Gson().fromJson(jsonTemp, PaperInfoBean.class));
                        }
                    }
                });
                commonDialog.show();
            }
        }
        //手势监听，滑动关闭软键盘
        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动距离超过**像素就收起键盘
                if (Math.abs(distanceY) > 5) {
                    KeyBoardUtils.hideKeyBoard(scrollView, actContext());
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

    private JzrPopupAdapter jzrPopupAdapter;
    //填写就诊人 动态提示
    private void initJzrPopup() {
        if (jzrPopupAdapter != null) {
            jzrPopupAdapter.notifyDataSetChanged();
        }

        jzrPopupAdapter = new JzrPopupAdapter(this, jzrList);
        etName.setAdapter(jzrPopupAdapter);
        etName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientFamilyBean.JiuzhenBean bean = (PatientFamilyBean.JiuzhenBean) parent.getAdapter().getItem(position);
                chooseJzrPopup(bean);
            }
        });
    }

    //输入姓名监听
    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        if (s.toString().trim().length() == 11 && !isChoosePatient) {//先 匹配手机号
            mPresenter.getJZRByPhone(s.toString());
        }
    }

    //服务费
    private int serverMoney = 0;
    //副数
    private int drugNum = 1;
    //单幅要的价格
    private double drugMoney = 0d;
    //输入服务费监听
    @OnTextChanged(value = R.id.et_serverprice, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterServerChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            serverMoney = 0;
        } else {
            serverMoney = Integer.parseInt(s.toString().trim());
        }
        //费用明细-诊疗费
        tvMoneyService.setText(serverMoney + "元");
        updateTotalMoney();
    }

    //输入副数
    @OnTextChanged(value = R.id.et_num, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterNumChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            drugNum = 1;
        } else {
            drugNum = Integer.parseInt(s.toString().trim());
            if (drugNum < 1) {
                drugNum = 1;
            }
        }
        //中药 && 副数≥5 可代煎
        if (drugNum < 5 && "ZY".equals(drugType) && rbYes.isChecked()) {
            rbNo.setChecked(true);
        }
        updateTotalMoney();
    }

    //计算总价
    private void updateTotalMoney() {
        //药材数量，每副价格
        tvDrugInfo.setText(String.format("共%s味、每副%s元", drugBeans.size(), String.format("%.2f", drugMoney)));
        //药材费用
        tvMoneyDrug.setText(String.format("%s元/副x%s副=%s元", String.format("%.2f", drugMoney), drugNum, String.format("%.2f", drugMoney * drugNum)));
        //总价
        tvMoneyTotal.setText(String.format("%.2f", serverMoney + drugMoney * drugNum) + "元");
    }

    //初始base数据
    private void initBaseData() {
        baseBean = U.getOpenpapeBaseData();
        //基础数据空的时候
        if (null == baseBean || null == baseBean.store || null == baseBean.drug_class
                || null == baseBean.usage || null == baseBean.frequency) {
            commonDialog = new CommonDialog(this, true, "数据异常，请退出后重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_BASEDATA_NULL));
                    finish();
                }
            });
            commonDialog.show();
        }

        //药房
        for (OPenPaperBaseBean.StoreBean bean : baseBean.store) {
            drugStoreList.add(bean.drug_store_name);
        }
        //药房默认选择第一
        if (!drugStoreList.isEmpty()) {
            storeId = baseBean.store.get(0).drug_store_id;
            etDrugstore.setText(baseBean.store.get(0).drug_store_name);
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
        //是否可见
        menbSeeList = Arrays.asList(UIUtils.getArray(R.array.memb_see));
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
            R.id.tv_adddrug, R.id.et_usetype, R.id.tv_addcommpaper, R.id.et_memb_see, R.id.et_docadvice, R.id.tv_next_step})
    public void tabOnClick(View view) {
        //防止多次点击
        if (UIUtils.isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_addpatient://选择患者
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.online_choosepatient);
                isChoosePatient = true;
                Intent intent = new Intent();
                if (formParent == 1) {
                    intent.setClass(this, PatientFamilyActivity.class);
                    intent.putExtra("memb_no", membNo);
                } else {
                    intent.setClass(this, JZRListActivity.class);
                }
                intent.putExtra("formtype", 1);//来自 选择患者
                startActivity(intent);
                break;
            case R.id.tv_editepatient://编辑就诊人
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.online_writepatient);
                isChoosePatient = false;
                writeJzInfo();
                break;
            case R.id.tv_addcommpaper://添加为常用处方
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.online_add_commpaper);
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
            case R.id.et_drugstore://药房
                //是否选过了药房 切换药房 要提醒
                if (TextUtils.isEmpty(etDrugstore.getText()) || drugBeans.size() == 0) {
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
                //汤剂副数>5可代煎，其他（膏，散，丸）必须代煎（隐藏不可选）
                bottomPopupView = new BottomListPopupView(this, "请选择剂型", drugClassList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        drugClassId = baseBean.drug_class.get(position).id;
                        etDrugClass.setText(drugClassList.get(position));

                        if (position == 0) {
                            //汤剂可选择是否代煎
                            rltDaijian.setVisibility(View.VISIBLE);
                            daijianType = 1;
                            rgDaijian.check(R.id.rb_no);
                        } else {
                            //汤剂之外的 必须代煎，隐藏不可选
                            rltDaijian.setVisibility(View.GONE);
                            daijianType = 0;
                        }
                    }
                });
                bottomPopupView.show(scrollView);
                break;
            case R.id.et_memb_see://购药前是否可见
                bottomPopupView = new BottomListPopupView(this, "购药前是否可见", menbSeeList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        membSee = position;
                        etMembSee.setText(menbSeeList.get(position));
                    }
                });
                bottomPopupView.show(scrollView);
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
            case R.id.et_docadvice://服药时间和禁忌
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
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.online_submit);
                checkData();
                break;
        }
    }

    //打开选择药房
    private void chooseDrugStore() {
        bottomPopupView = new BottomListPopupView(this, "请选择药房", drugStoreList, new BottomListPopupView.OnClickListener() {
            @Override
            public void selectItem(int pos) {
                storeId = baseBean.store.get(pos).drug_store_id;
                etDrugstore.setText(drugStoreList.get(pos));
            }
        });
        bottomPopupView.show(scrollView);
    }

    //选择的患者的就诊人（第一类）
    private void chooseJzInfo(PatientFamilyBean.JiuzhenBean bean) {
        lltJZinfo.setVisibility(View.VISIBLE);
        etName.setText(RegexUtil.getNameSubString(bean.patient_name));
        etPhone.setText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        membNo = bean.id;
        relationship = bean.relationship;
        pAccid = bean.getIm_accid();//记录需要
        etName.setEnabled(false);
        etPhone.setEnabled(false);

        //默认及就诊人的时候（性别，年龄 是空，可以修改）
        if (bean.sex == 0 || bean.sex == 1) {
            sexType = bean.sex;
            rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
            rbNan.setEnabled(false);
            rbNv.setEnabled(false);
        } else {
            //在线开方 选择默认就诊人，默认性别不选择
            sexType = bean.sex;//-1
            //默认就诊人可以修改
            rgSex.check(0);
            rbNan.setEnabled(true);
            rbNv.setEnabled(true);
        }
        //年龄
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        etAge.setEditeEnable(bean.age <= 0);
    }

    //手写就诊人信息
    private void writeJzInfo() {
        lltJZinfo.setVisibility(View.VISIBLE);
        etName.setEnabled(true);
        etAge.setEditeEnable(true);
        etPhone.setEnabled(true);
        rbNan.setEnabled(true);
        rbNv.setEnabled(true);
        relationship = 4;//关系 其他
        membNo = "";
        pAccid = "";
        etName.setText("");
        etAge.setEditeText("");
        etPhone.setText("");
        rgSex.check(R.id.rb_nan);
        //【电话】自动打开键盘
        KeyBoardUtils.showKeyBoard(etPhone, this);
    }

    //选择 匹配的就诊人（第二类）
    private void chooseJzrPopup(PatientFamilyBean.JiuzhenBean bean) {
//        etPhone.setText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        etName.setText(RegexUtil.getNameSubString(bean.patient_name));
        etName.setSelection(etName.getText().length());
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
        relationship = 4;//关系 其他
        membNo = "";
        pAccid = "";
        KeyBoardUtils.hideKeyBoard(etName, this);
    }

    //数据检测
    private void checkData() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())
                || sexType < 0
                || TextUtils.isEmpty(etPhone.getText().toString().trim())
                || TextUtils.isEmpty(etAge.getEditText().getText())) {
            commonDialog = new CommonDialog(this, "请填写就诊人信息");
            commonDialog.show();
            return;
        }
        if (TextUtils.isEmpty(etDrugstore.getText())) {
            commonDialog = new CommonDialog(this, "请选择药房");
            commonDialog.show();
            return;
        }
        if (drugBeans == null || drugBeans.isEmpty()) {
            commonDialog = new CommonDialog(this, "请添加药材");
            commonDialog.show();
            return;
        }

        switch (drugType) {
            case "ZY"://中草药
                if (TextUtils.isEmpty(etDrugClass.getText())) {
                    commonDialog = new CommonDialog(this, "请选择剂型");
                    commonDialog.show();
                    return;
                }
                if (TextUtils.isEmpty(etUsetype.getText())) {
                    commonDialog = new CommonDialog(this, "请选择用法用量");
                    commonDialog.show();
                    return;
                }
                if (TextUtils.isEmpty(etNum.getText().toString().trim()) || Integer.parseInt(etNum.getText().toString()) <= 0) {
                    commonDialog = new CommonDialog(this, "请填写副数");
                    commonDialog.show();
                    return;
                }
                break;
            case "ZCY"://中成药
            case "XY"://西药
                if (TextUtils.isEmpty(etUsetype.getText())) {
                    commonDialog = new CommonDialog(this, "请选择用法用量");
                    commonDialog.show();
                    return;
                }
                break;
            case "QC"://器材
                break;
        }

        if (TextUtils.isEmpty(docadviceStr)) {
            commonDialog = new CommonDialog(this, "请填写服药时间和禁忌");
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
        params.put("name", etName.getText().toString().trim());
        params.put("sex", sexType);
        params.put("age", etAge.getEditText().getText());
        params.put("phone", etPhone.getText().toString().trim());
        //主诉及辩证型
        if (!TextUtils.isEmpty(etSkillname.getText().toString().trim())) {
            params.put("icd10", etSkillname.getText().toString().trim());
        }
        params.put("store_id", storeId);
        params.put("param", new Gson().toJson(drugBeans));
        //备注
        params.put("remark", etRemark.getText().toString().trim());

        switch (drugType) {
            case "ZY"://中草药
                params.put("drug_class", drugClassId);//剂型
                params.put("boiled_type", daijianType);//代煎
                params.put("drug_num", Integer.parseInt(etNum.getText().toString()));//副数
                params.put("usages", usagesStr);//用法
                params.put("freq", freqStr);//用量
                break;
            case "ZCY"://中成药
            case "XY"://西药
                params.put("usages", usagesStr);//用法
                params.put("freq", freqStr);//用量
                break;
            case "QC"://器材
                break;
        }

        //补充收费
        if (!TextUtils.isEmpty(etServerprice.getText().toString().trim())) {
            params.put("service_price", etServerprice.getText().toString().trim());
        }
        params.put("doc_remark", docadviceStr);//医嘱
        params.put("memb_see", membSee);//处方是否可见
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
            case OpenPaperPresenter.ADD_COMMPAPER_OK:
                commonDialog = new CommonDialog(this, "添加常用处方成功");
                commonDialog.show();
                break;
            case OpenPaperPresenter.GET_PAPER_INFO_OK://获取处方详情ok
                PaperInfoBean infoBean = (PaperInfoBean) message.obj;
                editePaperInfoDate(infoBean);
                break;
            case OpenPaperPresenter.GET_JZR_BY_PHONE://提示 就诊人数据
                jzrList.clear();
                List<PatientFamilyBean.JiuzhenBean> list = (List<PatientFamilyBean.JiuzhenBean>) message.obj;
                if (list == null || list.isEmpty()) {//没有查询到 不显示
                    return;
                } else {
                    jzrList.addAll(list);
                    initJzrPopup();
                }
                break;
            case OpenPaperPresenter.OPENPAPER_ONLINE_OK://提交后
                HttpResponse<OnlinePaperBackBean> httpResponse = (HttpResponse<OnlinePaperBackBean>) message.obj;
                OnlinePaperBackBean bean = httpResponse.data;
                if (httpResponse.data == null) {//data为空  说明提交成功
                    //可以拿到paccid 就记录，没有就不记录
                    if (!TextUtils.isEmpty(pAccid)) {
                        mPresenter.addChatRecord(NimU.getNimAccount(), pAccid, Constant.CHAT_RECORD_TYPE_3, formParent);
                    }
                    if (formParent == 1) {//聊天开方
                        DocApplication.getAppComponent().dataRepo().appSP().remove(membNo);
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {//普通开方
                        String msgStr = TextUtils.isEmpty(httpResponse.msg) ? "处方提交成功，已通知患者支付" : httpResponse.msg;
                        commonDialog = new CommonDialog(this, true, msgStr, new View.OnClickListener() {
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
                    String msgError = TextUtils.isEmpty(httpResponse.msg) ? "药房缺少药材，请修改处方" : httpResponse.msg;
                    commonDialog = new CommonDialog(this, msgError);
                    commonDialog.show();
                }
                break;
        }
    }

    //处理【调用此方】的数据
    private void editePaperInfoDate(PaperInfoBean infoBean) {
        if (infoBean == null) {
            return;
        }
        membNo = infoBean.memb_no;
        relationship = infoBean.relationship;
        //就诊人
        isChoosePatient = true;
        lltJZinfo.setVisibility(View.VISIBLE);
        etName.setText(RegexUtil.getNameSubString(infoBean.name));
        etAge.setEditeText(TextUtils.isEmpty(infoBean.age) ? "" : infoBean.age);
        etPhone.setText(TextUtils.isEmpty(infoBean.phone) ? "" : infoBean.phone);
        etSkillname.setText(TextUtils.isEmpty(infoBean.icd10) ? "" : infoBean.icd10); //病症
        rgSex.check(infoBean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
        etName.setEnabled(false);
        etAge.setEditeEnable(false);
        etPhone.setEnabled(false);
        rbNan.setEnabled(false);
        rbNv.setEnabled(false);
        //药房id
        for (OPenPaperBaseBean.StoreBean storeBean : baseBean.store) {
            if (storeBean.drug_store_id == infoBean.store_id) {
                storeId = infoBean.store_id;
                etDrugstore.setText(storeBean.drug_store_name);
                break;
            }
        }
        //药材类型（“ZY”：”中草药” “ZCY”：”中成药” “XY” ：”西药” “QC” ：”器材”）
        drugType = infoBean.param.get(0).drug_type;
        setDrugType();
        switch (drugType) {
            case "ZY"://中草药
                //剂型
                drugClassId = infoBean.drug_class;
                for (OPenPaperBaseBean.CommBean classBean : baseBean.drug_class) {
                    if (classBean.id == drugClassId) {
                        etDrugClass.setText(classBean.name);
                        break;
                    }
                }
                //副数
                etNum.setText(infoBean.drug_num > 0 ? String.valueOf(infoBean.drug_num) : "1");
                if (drugClassId == baseBean.drug_class.get(0).id) {//汤剂
                    if (infoBean.drug_num < 5) {
                        //不可代煎
                        daijianType = 1;
                        rbNo.setChecked(true);
                    } else {
                        //代煎状态
                        daijianType = infoBean.boiled_type;
                        rgDaijian.check(daijianType == 0 ? R.id.rb_yes : R.id.rb_no);
                    }
                } else {//其他 必须代煎
                    rltDaijian.setVisibility(View.GONE);
                    daijianType = 0;
                }

                //用法，用量
                usagesStr = TextUtils.isEmpty(infoBean.usages) ? "" : infoBean.usages;
                freqStr = TextUtils.isEmpty(infoBean.freq) ? "" : infoBean.freq;
                if (!TextUtils.isEmpty(usagesStr) && !TextUtils.isEmpty(usagesStr)) {
                    etUsetype.setText(usagesStr + "-" + freqStr);
                }
                break;
            case "XY"://西药
            case "ZCY"://中成药
                //用法，用量
                usagesStr = TextUtils.isEmpty(infoBean.usages) ? "" : infoBean.usages;
                freqStr = TextUtils.isEmpty(infoBean.freq) ? "" : infoBean.freq;
                if (!TextUtils.isEmpty(usagesStr) && !TextUtils.isEmpty(usagesStr)) {
                    etUsetype.setText(usagesStr + "-" + freqStr);
                }
                break;
            case "QC"://器材
                break;
        }
        //是否可见
        membSee = infoBean.memb_see >= 0 ? infoBean.memb_see : 0;
        etMembSee.setText(menbSeeList.get(membSee));

        //诊疗费
        etServerprice.setText(infoBean.service_price > 0 ? String.valueOf(infoBean.service_price) : "");
        //医嘱
        docadviceStr = TextUtils.isEmpty(infoBean.doc_remark) ? "" : infoBean.doc_remark;
        etDocadvice.setText(docadviceStr);
        //备注
        etRemark.setText(TextUtils.isEmpty(infoBean.remark) ? "" : infoBean.remark); //备注
        //药材
        setDrugBeans(infoBean.param);
    }

    //设置全部药材，计算药材价格
    private void setDrugBeans(List<DrugBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            drugBeans.clear();
            drugBeans.addAll(beans);
            adapter.notifyDataSetChanged();
            //计算药品价格
            drugMoney = 0;
            for (DrugBean bean : drugBeans) {
                drugMoney += bean.price * bean.drug_num;
            }
            updateTotalMoney();
        }
        //保存长用处方 是否显示
        tvAddcommpaper.setVisibility(drugBeans.isEmpty() ? View.GONE : View.VISIBLE);
    }

    //不同的type UI展示不同
    private void setDrugType() {
        switch (drugType) {
            case "ZY"://中草药
                etDrugClass.setVisibility(View.VISIBLE);//剂型
                rltNum.setVisibility(View.VISIBLE);//副数
                etUsetype.setVisibility(View.VISIBLE);//用法用量
                //默认汤剂
                if (drugClassId == -1) {
                    drugClassId = baseBean.drug_class.get(0).id;
                    etDrugClass.setText(drugClassList.get(0));
                    rltDaijian.setVisibility(View.VISIBLE);//代煎
                }
                break;
            case "ZCY"://中成药
            case "XY"://西药
                etDrugClass.setVisibility(View.GONE);//剂型
                rltDaijian.setVisibility(View.GONE);//代煎
                rltNum.setVisibility(View.GONE);//副数
                etUsetype.setVisibility(View.VISIBLE);//用法用量 显示
                break;
            case "QC"://器材
                etDrugClass.setVisibility(View.GONE);//剂型
                rltDaijian.setVisibility(View.GONE);//代煎
                rltNum.setVisibility(View.GONE);//副数
                etUsetype.setVisibility(View.GONE);//副数
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHOOSE_PATIENT://选择患者-就诊人
            case EventConfig.EVENT_KEY_CHOOSE_JZR://选择就诊人
                PatientFamilyBean.JiuzhenBean bean = (PatientFamilyBean.JiuzhenBean) event.getData();
                if (bean != null) {
                    chooseJzInfo(bean);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_ADDDRUG://添加药材
                //显示药材
                setDrugBeans(data.getParcelableArrayListExtra("druglist"));
                //药材类型
                drugType = data.getStringExtra("drug_type");
                setDrugType();
                break;
            case REQUEST_CODE_DOCADVICE://医嘱
                docadviceStr = data.getStringExtra("docadvice");
                etDocadvice.setText(docadviceStr);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //来自聊天，返回不提示，默认保存临时的处方数据
        if (formParent == 1) {
            saveTempPaperInfo();
            finish();
        } else {
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
    }

    //来自聊天，返回不提示，默认保存临时的处方数据
    private void saveTempPaperInfo() {
        //如果没有开药，不保存临时数据
        if (drugBeans == null || drugBeans.isEmpty()) {
            return;
        }
        PaperInfoBean tempBean = new PaperInfoBean();
        tempBean.memb_no = membNo;
        tempBean.relationship = relationship;
        //就诊人
        tempBean.name = etName.getText().toString().trim();
        tempBean.age = etAge.getEditText().getText().toString().trim();
        tempBean.phone = etPhone.getText().toString().trim();
        tempBean.icd10 = etSkillname.getText().toString().trim();
        tempBean.sex = rbNan.isChecked() ? 0 : 1;//男：0、女：1
        //药房id
        tempBean.store_id = storeId;
        //剂型
        tempBean.drug_class = drugClassId;
        //代煎
        tempBean.boiled_type = daijianType;
        //副数
        tempBean.drug_num = TextUtils.isEmpty(etNum.getText().toString().trim()) ? 1 : Integer.parseInt(etNum.getText().toString().trim());
        //用法，用量
        tempBean.usages = usagesStr;
        tempBean.freq = freqStr;
        //诊疗费
        tempBean.service_price = TextUtils.isEmpty(etServerprice.getText().toString().trim()) ? 0 : Integer.parseInt(etServerprice.getText().toString().trim());
        //医嘱
        tempBean.doc_remark = docadviceStr;
        //药材
        tempBean.param = drugBeans;

        //key-membNo 保存临时处方数据
        DocApplication.getAppComponent().dataRepo().appSP().setString(membNo, new Gson().toJson(tempBean));
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
