package com.renxin.doctor.activity.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.bean_jht.BaseConfigBean;

import java.util.List;

/**
 * 擅长疾病
 * SkillExpandAdapter
 * Create at 2018/4/4 上午11:40 by mayakun
 */
public class SkillExpandAdapter extends BaseMultiItemQuickAdapter<BaseConfigBean.Skill, BaseViewHolder> {


    public SkillExpandAdapter(List<BaseConfigBean.Skill> data) {
        super(data);
        addItemType(0, R.layout.item_skill);//默认0
        addItemType(1, R.layout.item_skill_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseConfigBean.Skill item) {
        switch (item.getItemType()) {
            case 0:
                helper.setText(R.id.tv_skillname, item.name);
                helper.getView(R.id.tv_skillname).setSelected(item.isSelect);
                break;
            case 1:
                helper.setText(R.id.tv_skilltitle, item.name);
                break;
        }
    }


}
