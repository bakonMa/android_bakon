package com.renxin.doctor.activity.ui.bean;

import java.io.Serializable;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class ApplyAuthorizationBean implements Serializable {


    /**
     * lainlianDTO : {"code":"200","msg":"成功","model":{"state":"SUCCESS","stateMsg":"交易成功","token":"743636A186303217AA378CE1F1480161","platformUserNo":"SLJR1031000000015848","userId":"0001887421218030","accessType":"FULL_CHECKED","auditStatus":"PASSED"}}
     * applyAuthorizationDTO : {"id":30,"orderNo":"(沪)20171214第001号","userName":"赵贇","idCard":"43010319820804053X","bankCardNo":"6217711603557719","bankCardName":"中信银行","bankPhone":"18874212180","password":"","userType":"1","contributiveEnum":"01"}
     */

    private LainlianDTOBean lainlianDTO;
    private ApplyAuthorizationDTOBean applyAuthorizationDTO;

    public LainlianDTOBean getLainlianDTO() {
        return lainlianDTO;
    }

    public void setLainlianDTO(LainlianDTOBean lainlianDTO) {
        this.lainlianDTO = lainlianDTO;
    }

    public ApplyAuthorizationDTOBean getApplyAuthorizationDTO() {
        return applyAuthorizationDTO;
    }

    public void setApplyAuthorizationDTO(ApplyAuthorizationDTOBean applyAuthorizationDTO) {
        this.applyAuthorizationDTO = applyAuthorizationDTO;
    }

    public static class LainlianDTOBean implements Serializable {
        /**
         * code : 200
         * msg : 成功
         * model : {"state":"SUCCESS","stateMsg":"交易成功","token":"743636A186303217AA378CE1F1480161","platformUserNo":"SLJR1031000000015848","userId":"0001887421218030","accessType":"FULL_CHECKED","auditStatus":"PASSED"}
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

        public static class ModelBean implements Serializable{
            /**
             * state : SUCCESS
             * stateMsg : 交易成功
             * token : 743636A186303217AA378CE1F1480161
             * platformUserNo : SLJR1031000000015848
             * userId : 0001887421218030
             * accessType : FULL_CHECKED
             * auditStatus : PASSED
             */

            private String state;
            private String stateMsg;
            private String token;
            private String platformUserNo;
            private String userId;
            private String accessType;
            private String auditStatus;

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

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getPlatformUserNo() {
                return platformUserNo;
            }

            public void setPlatformUserNo(String platformUserNo) {
                this.platformUserNo = platformUserNo;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAccessType() {
                return accessType;
            }

            public void setAccessType(String accessType) {
                this.accessType = accessType;
            }

            public String getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(String auditStatus) {
                this.auditStatus = auditStatus;
            }
        }
    }

    public static class ApplyAuthorizationDTOBean implements Serializable{
        /**
         * id : 30
         * orderNo : (沪)20171214第001号
         * userName : 赵贇
         * idCard : 43010319820804053X
         * bankCardNo : 6217711603557719
         * bankCardName : 中信银行
         * bankPhone : 18874212180
         * password :
         * userType : 1
         * contributiveEnum : 01
         */

        private int id;
        private String orderNo;
        private String userName;
        private String idCard;
        private String bankCardNo;
        private String bankCardName;
        private String bankPhone;
        private String password;
        private String userType;
        private String contributiveEnum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getBankCardNo() {
            return bankCardNo;
        }

        public void setBankCardNo(String bankCardNo) {
            this.bankCardNo = bankCardNo;
        }

        public String getBankCardName() {
            return bankCardName;
        }

        public void setBankCardName(String bankCardName) {
            this.bankCardName = bankCardName;
        }

        public String getBankPhone() {
            return bankPhone;
        }

        public void setBankPhone(String bankPhone) {
            this.bankPhone = bankPhone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getContributiveEnum() {
            return contributiveEnum;
        }

        public void setContributiveEnum(String contributiveEnum) {
            this.contributiveEnum = contributiveEnum;
        }
    }
}
