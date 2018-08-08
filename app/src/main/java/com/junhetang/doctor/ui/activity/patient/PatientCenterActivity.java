package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.nim.message.SessionHelper;
import com.junhetang.doctor.ui.activity.mine.AuthStep1Activity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CheckPaperBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * PatientFamilyActivity 患者列表
 * Create at 2018/4/21 下午10:23 by mayakun
 */
public class PatientCenterActivity extends BaseActivity implements PatientContact.View {
    public static int REQUEST_CODE_REMARKNAME = 2010;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    PatientPresenter mPresenter;

    private int formType = 0;//是否来自选择患者(0 默认不是 1：选择患者)
    private String membNo;
    private PatientFamilyBean bean;
    private List<PatientFamilyBean.JiuzhenBean> jiuzhenBeans = new ArrayList<>();//就诊人数据
    private ArrayList<String> patientLsit = new ArrayList<>();//就诊人数据
    private List<CheckPaperBean> paperList = new ArrayList<>();//处方数据
    private BaseQuickAdapter mAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private CommonDialog commonDialog;
    private String im_accid;//选择就诊人时需要
    private int pageNum = 1;
    private int tempPos = 0;//spinner pos

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_patientcenter;
    }

    @Override
    protected void initView() {
        initToolbar();
        membNo = getIntent().getStringExtra("memb_no");
        formType = getIntent().getIntExtra("formtype", 0);
        im_accid = getIntent().getStringExtra("im_accid");


        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientLsit);
        spinner.setAdapter(spinnerAdapter);//绑定适配器到Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempPos = position;
                pageNum = 1;
                mPresenter.getPatientPaper(pageNum, tempPos == 0 ? "0" : jiuzhenBeans.get(tempPos - 1).id, membNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //列表数据
        recycleview.setLayoutManager(new LinearLayoutManager(actContext()));
        mAdapter = new BaseQuickAdapter<CheckPaperBean, BaseViewHolder>(R.layout.item_patient_paper, paperList) {
            @Override
            protected void convert(BaseViewHolder helper, CheckPaperBean item) {
                helper.setText(R.id.patient_name, "就诊人：" + item.patient_name + "     " + (item.sex == 0 ? "男" : "女") + "     " + item.age + "岁")
                        .setText(R.id.skill_name, "主述挤辩证：" + (TextUtils.isEmpty(item.bz_remark) ? "" : item.bz_remark))
                        .setText(R.id.service_money, "诊疗费：" + (TextUtils.isEmpty(item.service_price) ? "" : (item.service_price + "元")))
                        .setText(R.id.drug_money, "药材费：" + (TextUtils.isEmpty(item.total_drug) ? "" : (item.total_drug + "元")))
                        .setText(R.id.paper_date, "开方时间：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time));
            }
        };
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getPatientPaper(pageNum + 1, tempPos == 0 ? "0" : jiuzhenBeans.get(tempPos - 1).id, membNo);
            }
        }, recycleview);

        recycleview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Umeng 埋点
                MobclickAgent.onEvent(actContext(), UmengKey.patientcenter_itemclick);
                CheckPaperBean paperBean = paperList.get(position);
                boolean canuse = (paperBean.presc_type == 1 || (paperBean.presc_type == 2 && paperBean.z_status == 1));
                Intent intent = new Intent(actContext(), PaperH5Activity.class);
                intent.putExtra("hasTopBar", true);//是否包含toolbar
                intent.putExtra("canuse", canuse);//是否显示【调用此方】
                intent.putExtra("webType", PaperH5Activity.FORM_TYPE.H5_PAPER_DETAIL);
                intent.putExtra("title", UIUtils.getString(R.string.str_paper_detail));
                intent.putExtra("url", H5Config.H5_PAPER_DETAIL + paperBean.id);
                intent.putExtra("checkid", paperBean.id);
                startActivity(intent);
            }
        });

        mPresenter.getpatientFamily(membNo);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("患者中心")
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

    @OnClick({R.id.tv_remark, R.id.tv_gotochat})
    public void btnOnClick(View view) {
        //选择患者时不能修改其他都行
        if (formType == 1) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_gotochat:
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.patientcenter_chat);
                if (U.isHasAuthOK()) { //认证通过 进入聊天
                    //告诉 后台 医生主动聊天
                    mPresenter.docToTalk(im_accid);
                    SessionHelper.startP2PSession(actContext(), im_accid);
                } else {
                    commonDialog = new CommonDialog(this, R.layout.dialog_auth, U.getAuthStatusMsg(),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (view.getId() == R.id.btn_gotuauth) {
                                        startActivity(new Intent(PatientCenterActivity.this, AuthStep1Activity.class));
                                    }
                                }
                            });
                    commonDialog.show();
                }
                break;
            case R.id.tv_remark://设置备注
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.patientcenter_remark);
                Intent intent = new Intent(this, RemarkNameActivity.class);
                intent.putExtra("patientinfo", bean.patientinfo);
                startActivityForResult(intent, REQUEST_CODE_REMARKNAME);
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.GET_PATIENTFAMILY_0K:
                bean = (PatientFamilyBean) message.obj;
                if (bean != null && bean.patientinfo != null) {
                    ImageUtil.showCircleImage(bean.patientinfo.head_url, ivHead);
                    tvName.setText(TextUtils.isEmpty(bean.patientinfo.remark_name) ? bean.patientinfo.nick_name : bean.patientinfo.remark_name);
                    tvPhone.setText("手机号：" + (TextUtils.isEmpty(bean.patientinfo.phone) ? "" : bean.patientinfo.phone));
                    tvClass.setText(TextUtils.isEmpty(bean.patientinfo.memb_class) ? "" : bean.patientinfo.memb_class);
                }
                if (bean != null && bean.jiuzhen != null) {
                    jiuzhenBeans.clear();
                    jiuzhenBeans.addAll(bean.jiuzhen);
                    //spinner数据
                    patientLsit.add("全部");
                    for (PatientFamilyBean.JiuzhenBean jiuzhenBean : jiuzhenBeans) {
                        patientLsit.add(String.format("%s(%s)", jiuzhenBean.patient_name, Constant.RELATION_TYPE[jiuzhenBean.relationship]));
                    }
                    spinnerAdapter.notifyDataSetChanged();
                }
                break;
            case PatientPresenter.GET_PATIEN_PAPER_TLIST_0K:
                BasePageBean<CheckPaperBean> beans = (BasePageBean<CheckPaperBean>) message.obj;
                if (beans != null && beans.list != null) {
                    pageNum = beans.page;
                    if (pageNum == 1) {
                        paperList.clear();
                    }
                    paperList.addAll(beans.list);
                    mAdapter.notifyDataSetChanged();

                    if (beans.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
                if (paperList.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_REMARKNAME) {
            if (data != null) {
                bean.patientinfo.remark_name = data.getStringExtra("remarkname");
                tvName.setText(TextUtils.isEmpty(bean.patientinfo.remark_name) ? bean.patientinfo.nick_name : bean.patientinfo.remark_name);
            }
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

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
