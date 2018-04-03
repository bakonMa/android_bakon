package com.jht.doctor.data.http;

/**
 * @date 创建时间:2018/2/2
 * @Description 异常基类
 */
public class ApiException extends Exception {

    public static final String ERROR_API_1001 = "1001"; //未获取验证码
    public static final String ERROR_API_1002 = "1002"; //签名过期

    private String code;
    private String displayMessage;

    public ApiException() {
    }

    public ApiException(String message, String code, String displayMessage) {
        super(message);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public ApiException(String code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage == null ? "" : displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    @Override
    public String getMessage() {
        return super.getMessage() == null ? displayMessage : super.getMessage();
    }
}
