package com.jht.doctor.ui.activity.mine.myinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyImageInfoActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_name)
    TextView idTvName;
    @BindView(R.id.id_tv_phone)
    TextView idTvPhone;

    public static final String NAME_KEY = "name";

    public static final String PHONE_KEY = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image_info);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initData() {
        if (getIntent().getStringExtra(NAME_KEY) != null) {
            idTvName.setText(RegexUtil.hideFirstName(getIntent().getStringExtra(NAME_KEY)));
        }
        if (getIntent().getStringExtra(PHONE_KEY) != null) {
            idTvPhone.setText(RegexUtil.hidePhone(getIntent().getStringExtra(PHONE_KEY)));
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("个人中心")
                .setLeft(false)
                .setStatuBar(R.color.white)
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

    }
}
