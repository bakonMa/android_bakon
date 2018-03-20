package com.jht.doctor.ui.bean;

/**
 * Created by table on 2017/11/22.
 * description:
 */

public class LoginResponse {

    /**
     * applyJudgeDTO : {"applyStatus":false,"orderNo":"string","preTrialAmt":0,"preTrialStatus":false,"repaymentStatus":false}
     * errorCause : string
     * isFirstLogin : false
     * token : string
     * userInfoDTO : {"userDTO":{"certNo":"string","certType":"string","userName":"string"},"userHouseDTO":{"areaCode":"string","cityCode":"string","communityName":"string","detailAddress":"string","hasLoan":"string","houseArea":0,"houseType":"string","loanAmt":0,"loanOrg":"string","provinceCode":"string"},"userJobDTO":{"companyName":"string","companyType":"string","industry":"string","monthlyIncome":0,"position":"string"}}
     */

    private ApplyJudgeDTOBean applyJudgeDTO;
    private String errorCause;
    private boolean isFirstLogin;
    private String token;
    private UserInfoDTOBean userInfoDTO;

    public ApplyJudgeDTOBean getApplyJudgeDTO() {
        return applyJudgeDTO;
    }

    public void setApplyJudgeDTO(ApplyJudgeDTOBean applyJudgeDTO) {
        this.applyJudgeDTO = applyJudgeDTO;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public boolean isIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoDTOBean getUserInfoDTO() {
        return userInfoDTO;
    }

    public void setUserInfoDTO(UserInfoDTOBean userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }

    public static class ApplyJudgeDTOBean {
        /**
         * applyStatus : false
         * orderNo : string
         * preTrialAmt : 0
         * preTrialStatus : false
         * repaymentStatus : false
         */

        private boolean applyStatus;
        private String orderNo;
        private double preTrialAmt;
        private boolean preTrialStatus;
        private boolean repaymentStatus;

        public boolean isApplyStatus() {
            return applyStatus;
        }

        public void setApplyStatus(boolean applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public double getPreTrialAmt() {
            return preTrialAmt;
        }

        public void setPreTrialAmt(double preTrialAmt) {
            this.preTrialAmt = preTrialAmt;
        }

        public boolean isPreTrialStatus() {
            return preTrialStatus;
        }

        public void setPreTrialStatus(boolean preTrialStatus) {
            this.preTrialStatus = preTrialStatus;
        }

        public boolean isRepaymentStatus() {
            return repaymentStatus;
        }

        public void setRepaymentStatus(boolean repaymentStatus) {
            this.repaymentStatus = repaymentStatus;
        }
    }

    public static class UserInfoDTOBean {
        /**
         * userDTO : {"certNo":"string","certType":"string","userName":"string"}
         * userHouseDTO : {"areaCode":"string","cityCode":"string","communityName":"string","detailAddress":"string","hasLoan":"string","houseArea":0,"houseType":"string","loanAmt":0,"loanOrg":"string","provinceCode":"string"}
         * userJobDTO : {"companyName":"string","companyType":"string","industry":"string","monthlyIncome":0,"position":"string"}
         */

        private UserDTOBean userDTO;
        private UserHouseDTOBean userHouseDTO;
        private UserJobDTOBean userJobDTO;

        public UserDTOBean getUserDTO() {
            return userDTO;
        }

        public void setUserDTO(UserDTOBean userDTO) {
            this.userDTO = userDTO;
        }

        public UserHouseDTOBean getUserHouseDTO() {
            return userHouseDTO;
        }

        public void setUserHouseDTO(UserHouseDTOBean userHouseDTO) {
            this.userHouseDTO = userHouseDTO;
        }

        public UserJobDTOBean getUserJobDTO() {
            return userJobDTO;
        }

        public void setUserJobDTO(UserJobDTOBean userJobDTO) {
            this.userJobDTO = userJobDTO;
        }

        public static class UserDTOBean {
            /**
             * certNo : string
             * certType : string
             * userName : string
             */

            private String certNo;
            private String certType;
            private String userName;

            public String getCertNo() {
                return certNo;
            }

            public void setCertNo(String certNo) {
                this.certNo = certNo;
            }

            public String getCertType() {
                return certType;
            }

            public void setCertType(String certType) {
                this.certType = certType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        public static class UserHouseDTOBean {
            /**
             * areaCode : string
             * cityCode : string
             * communityName : string
             * detailAddress : string
             * hasLoan : string
             * houseArea : 0
             * houseType : string
             * loanAmt : 0
             * loanOrg : string
             * provinceCode : string
             */

            private String areaCode;
            private String cityCode;
            private String communityName;
            private String detailAddress;
            private String hasLoan;
            private double houseArea;
            private String houseType;
            private double loanAmt;
            private String loanOrg;
            private String provinceCode;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCommunityName() {
                return communityName;
            }

            public void setCommunityName(String communityName) {
                this.communityName = communityName;
            }

            public String getDetailAddress() {
                return detailAddress;
            }

            public void setDetailAddress(String detailAddress) {
                this.detailAddress = detailAddress;
            }

            public String getHasLoan() {
                return hasLoan;
            }

            public void setHasLoan(String hasLoan) {
                this.hasLoan = hasLoan;
            }

            public double getHouseArea() {
                return houseArea;
            }

            public void setHouseArea(double houseArea) {
                this.houseArea = houseArea;
            }

            public String getHouseType() {
                return houseType;
            }

            public void setHouseType(String houseType) {
                this.houseType = houseType;
            }

            public double getLoanAmt() {
                return loanAmt;
            }

            public void setLoanAmt(double loanAmt) {
                this.loanAmt = loanAmt;
            }

            public String getLoanOrg() {
                return loanOrg;
            }

            public void setLoanOrg(String loanOrg) {
                this.loanOrg = loanOrg;
            }

            public String getProvinceCode() {
                return provinceCode;
            }

            public void setProvinceCode(String provinceCode) {
                this.provinceCode = provinceCode;
            }
        }

        public static class UserJobDTOBean {
            /**
             * companyName : string
             * companyType : string
             * industry : string
             * monthlyIncome : 0
             * position : string
             */

            private String companyName;
            private String companyType;
            private String industry;
            private double monthlyIncome;
            private String position;

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getCompanyType() {
                return companyType;
            }

            public void setCompanyType(String companyType) {
                this.companyType = companyType;
            }

            public String getIndustry() {
                return industry;
            }

            public void setIndustry(String industry) {
                this.industry = industry;
            }

            public double getMonthlyIncome() {
                return monthlyIncome;
            }

            public void setMonthlyIncome(double monthlyIncome) {
                this.monthlyIncome = monthlyIncome;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }
        }
    }
}