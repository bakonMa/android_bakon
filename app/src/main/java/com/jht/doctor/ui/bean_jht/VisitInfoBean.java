package com.jht.doctor.ui.bean_jht;

/**
 * AuthInfoBean 认证信息
 * Create at 2018/4/9 下午5:56 by mayakun
 */
public class VisitInfoBean {

    /**
     * first_diagnose : 101
     * second_diagnose : 50
     * fee_explain : 1.您可通过图文、语音与患者交流，首次回复需在24小时内，默认单次交流时间72小时，与患者沟通后，经双方同意可随时结束对话。
     * 2.患者首次咨询后，下次咨询将按复诊价格收取，您可自定义初诊、复诊价格，建议为老患者提供适当的优惠，您还可以进入患者列表，给某个患者自定义价格。
     * 3.若患者未在线上进行咨询，您直接为患者开方，则可在开方时补充服务费。
     * 4.咨询过程中给患者发送问诊单或随诊单不收取任何费用。若咨询结束或给从未在线咨询的患者发送问诊单或随诊单时，按初诊或复诊价格向患者收取咨询费用。
     */

    public String first_diagnose;
    public String second_diagnose;
    public String fee_explain;
}
