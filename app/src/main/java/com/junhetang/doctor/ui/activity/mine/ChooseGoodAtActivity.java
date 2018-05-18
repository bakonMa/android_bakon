package com.junhetang.doctor.ui.activity.mine;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.adapter.SkillExpandAdapter;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean_jht.BaseConfigBean;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 擅长疾病
 * ChooseGoodAtActivity
 * Create at 2018/4/4 上午9:57 by mayakun
 */
public class ChooseGoodAtActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tag_recyclerview)
    RecyclerView tagRecyclerview;
    @BindView(R.id.skill_recyclerview)
    RecyclerView skillRecyclerview;

    private BaseQuickAdapter tagAdapter;
    private SkillExpandAdapter skillAdapter;
    private ArrayList<BaseConfigBean.SkillsBean> skillList = new ArrayList<>();
    private List<BaseConfigBean.Skill> showList = new ArrayList<>();
    private ArrayList<BaseConfigBean.Skill> selectBeans = new ArrayList<>();

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_choosegootat;
    }

    @Override
    protected void initView() {
        initToolbar();
        //全部的疾病数据
        skillList = getIntent().getParcelableArrayListExtra("skills");
        //选中的bean
        selectBeans = getIntent().getParcelableArrayListExtra("selectskill");
        if (selectBeans == null) {
            selectBeans = new ArrayList<>();
        }
        //这种办法要3重循环 好low 以后想办法改进
        for (BaseConfigBean.SkillsBean skillsBean : skillList) {
            //title
            showList.add(new BaseConfigBean.Skill(skillsBean.category, 1));
            //疾病
            for (BaseConfigBean.Skill skill : skillsBean.name) {
                for (int i = 0; i < selectBeans.size(); i++) {
                    if(skill.id == selectBeans.get(i).id){
                        skill.isSelect = true;
                        //对象要一样
                        selectBeans.set(i, skill);
                    }
                }
            }
            showList.addAll(skillsBean.name);
        }

        //选择的条件标签
        tagAdapter = new BaseQuickAdapter<BaseConfigBean.Skill, BaseViewHolder>(R.layout.item_skill, selectBeans) {
            @Override
            protected void convert(BaseViewHolder helper, BaseConfigBean.Skill tagBean) {
                helper.setText(R.id.tv_skillname, tagBean.name);
                helper.getView(R.id.tv_skillname).setSelected(true);
            }
        };
        tagRecyclerview.setAdapter(tagAdapter);
        tagRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        tagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int showPos = showList.indexOf(selectBeans.get(position));
                showList.get(showPos).isSelect = false;
                skillAdapter.notifyItemChanged(showPos);
                //删除放在后面
                selectBeans.remove(position);
                tagAdapter.notifyDataSetChanged();
            }
        });

        //疾病
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return showList.get(position).getItemType() == 1 ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        skillAdapter = new SkillExpandAdapter(showList);
        skillRecyclerview.setAdapter(skillAdapter);
        //### important! setLayoutManager should be called after setAdapter###
        skillRecyclerview.setLayoutManager(gridLayoutManager);
        skillRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showList.get(position).getItemType() == 1) {
                    return;
                }
                if (showList.get(position).isSelect) {
                    selectBeans.remove(showList.get(position));
                    tagAdapter.notifyDataSetChanged();
                } else {
                    if (selectBeans.size() >= 3) {
                        ToastUtil.showShort("擅长疾病最多选择3项");
                        return;
                    }
                    selectBeans.add(showList.get(position));
                    tagAdapter.notifyDataSetChanged();
                }
                //取反
                showList.get(position).isSelect = !showList.get(position).isSelect;
                skillAdapter.notifyItemChanged(position);
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("擅长疾病")
                .setLeft(false)
                .setRightText("保存", true, R.color.color_main)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("skills", selectBeans);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).bind();
    }


    @Override
    protected void setupActivityComponent() {
    }

}
