package com.bakon.android.ui.bean;

import java.util.List;

/**
 * OnlinePaperBackBean 在线开方，提交后，药和药房不匹配
 * Create at 2018/5/14 下午5:05 by mayakun
 */

public class OnlinePaperBackBean {
    public int status;//status为-4时返回处方中不能用的药品param
    public List<DrugBean> param;

}
