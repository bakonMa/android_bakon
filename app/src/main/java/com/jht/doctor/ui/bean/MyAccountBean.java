package com.jht.doctor.ui.bean;

import java.util.List;

/**
 * Created by mayakun on 2017/12/7
 * 我的账户 bean
 */

public class MyAccountBean {

    public double currentRepaymentTotal;
    public double otherRepaymentTotal;
    public List<AccountItem> accountItem;

    public MyAccountBean(double currentRepaymentTotal, double otherRepaymentTotal) {
        this.currentRepaymentTotal = currentRepaymentTotal;
        this.otherRepaymentTotal = otherRepaymentTotal;
    }

    public static class AccountItem {
        public String orderNo;//订单号
        public String platformUserNo;//平台账户系统id
        public String userName;//用户名
        public int userRole;//用户角色 0 表示 主借人 1 表示共借人
        public String userBankNo;//银行卡号
        public double availableCredit;//账户余额(可用金额)
        public double totalRepaymentAmt;//应还余额(该标总还款金额)

        public AccountItem(String userName, int userRole, String userBankNo, double availableCredit, double totalRepaymentAmt) {
            this.userName = userName;
            this.userRole = userRole;
            this.userBankNo = userBankNo;
            this.availableCredit = availableCredit;
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }

}
