package com.junhetang.doctor.config;

/**
 * Created by table on 2017/12/6.
 * description:订单状态
 */

public interface OrderStatue {
    /**
     * 101：预审拒绝
     * 201：待申请(预审通过)
     * 202：申请取消
     * 301：实地征信(已申请)
     * 401：补充资料(实地征信通过)
     * 501：终审(补充资料通过)
     * 502：终审拒绝
     * 601：签约(终审通过)
     * 602：签约拒绝
     * 603：退回补件
     * 701：抵押(签约通过)
     * 702：抵押拒绝
     * 801：待放款(抵押通过)
     * 901：待还款(已放款)
     * 902：已结清
     */
    String PRE_REJECT = "101";
    String PRE_APPLY = "201";
    String APPLY_REJECT = "202";
    String ALREADY_APPLY = "301";
    String ADD_INFO = "401";
    String ADD_INFO_SUCCESS = "501";
    String TERMINAL_REFUSED = "502";
    String TERMINAL_SUCCESS = "601";
    String SIGN_REFUSED = "602";
    String RETURN_INFO = "603";
    String SIGN_SUCCESS = "701";
    String SIGN_FAILURED = "702";
    String PENDING_MONEY = "801";
    String REPAYMENT = "901";
    String END = "902";

}
