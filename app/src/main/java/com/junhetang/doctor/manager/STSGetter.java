package com.junhetang.doctor.manager;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.junhetang.doctor.ui.bean.OssTokenBean;

/**
 * STSGetter
 * Create at 2018/7/10 上午11:03 by mayakun
 */
public class STSGetter extends OSSFederationCredentialProvider {

    /*StatusCode：表示获取Token的状态，获取成功时，返回值是200。
     * AccessKeyId：表示Android/iOS应用初始化OSSClient获取的 AccessKeyId。
     * AccessKeySecret：表示Android/iOS应用初始化OSSClient获取AccessKeySecret。
     * SecurityToken：表示Android/iOS应用初始化的Token。
     * Expiration：表示该Token失效的时间。主要在Android SDK会自动判断是否失效，自动获取Token。*/

    private OSSFederationToken ossFederationToken;
    private String ak;
    private String sk;
    private String token;
    private String expiration;

    public STSGetter(OssTokenBean bean) {
        this.ak = bean.AccessKeyId;
        this.sk = bean.AccessKeySecret;
        this.token = bean.SecurityToken;
        this.expiration = bean.Expiration;
    }

    //获取token
    public OSSFederationToken getFederationToken() {
        return ossFederationToken = new OSSFederationToken(ak, sk, token, expiration);
    }
}