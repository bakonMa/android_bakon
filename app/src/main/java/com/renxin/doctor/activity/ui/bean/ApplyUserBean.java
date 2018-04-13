package com.renxin.doctor.activity.ui.bean;

/**
 * Created by table on 2017/12/14.
 * description:授权成功返回bean
 */

public class ApplyUserBean {

    /**
     * code : 200
     * msg : 成功
     * model : {"state":"SUCCESS","stateMsg":"交易成功"}
     */

    private String code;
    private String msg;
    private ModelBean model;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }

    public static class ModelBean {
        /**
         * state : SUCCESS
         * stateMsg : 交易成功
         */

        private String state;
        private String stateMsg;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateMsg() {
            return stateMsg;
        }

        public void setStateMsg(String stateMsg) {
            this.stateMsg = stateMsg;
        }
    }
}
