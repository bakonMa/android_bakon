package com.junhetang.doctor.ui.activity.home;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * ChooseDocAdviceActivity  选择医嘱
 * Create at 2018/4/26 上午9:57 by mayakun
 */
public class ChooseDocAdviceActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.et_advidce)
    EditText etAdvidce;

    private ArrayList<OPenPaperBaseBean.DocadviceBean> dataBeans = new ArrayList<>();
    private ArrayList<DocAdvice> showBeans = new ArrayList<>();
    private BaseSectionQuickAdapter adapter;
    private String docadviceStr;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_doc_advice;
    }

    @Override
    protected void initView() {
        initToolbar();
        docadviceStr = getIntent().getStringExtra("docadvice");
        dataBeans = getIntent().getParcelableArrayListExtra("beanlist");
        for (OPenPaperBaseBean.DocadviceBean bean : dataBeans) {
            showBeans.add(new DocAdvice(true, bean.title));
            for (int i = 0; i < bean.content.length; i++) {
                showBeans.add(new DocAdvice(bean.content[i]));
            }
        }
        if (!TextUtils.isEmpty(docadviceStr)) {
            etAdvidce.setText(docadviceStr);
            etAdvidce.setSelection(docadviceStr.length());
        }

        adapter = new BaseSectionQuickAdapter<DocAdvice, BaseViewHolder>(R.layout.item_doc_advice,
                R.layout.item_doc_advice_head, showBeans) {
            @Override
            protected void convert(BaseViewHolder helper, DocAdvice item) {
                helper.setText(R.id.tv_content, item.subContent);
            }

            @Override
            protected void convertHead(BaseViewHolder helper, DocAdvice item) {
                helper.setText(R.id.tv_title, item.title);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return showBeans.get(position).isHeader ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerview.setAdapter(adapter);
        //### important! setLayoutManager should be called after setAdapter###
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showBeans.get(position).isHeader) {//头部不能点击
                    return;
                }
                String temp = etAdvidce.getText().toString();
                etAdvidce.setText(TextUtils.isEmpty(temp) ? showBeans.get(position).subContent : (temp + "，" + showBeans.get(position).subContent));
                etAdvidce.setSelection(etAdvidce.getText().toString().length());
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("医嘱")
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
                        if (TextUtils.isEmpty(etAdvidce.getText().toString())) {
                            ToastUtil.showShort("请填写医嘱内容");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("docadvice", etAdvidce.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .bind();
    }


    @Override
    protected void setupActivityComponent() {
    }


    //医嘱展示
    class DocAdvice extends SectionEntity<String> {
        public String title;
        public String subContent;


        public DocAdvice(boolean isHeader, String header) {
            super(isHeader, header);
            this.title = header;
        }

        public DocAdvice(String s) {
            super(s);
            this.subContent = s;
        }
    }
}
