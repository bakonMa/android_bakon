package com.junhetang.doctor.ui.bean;

/**
 * OssTokenBean oss的数据bean
 * Create at 2018/7/10 上午10:57 by mayakun
 * https://help.aliyun.com/document_detail/31920.html
 */
public class OssTokenBean {

    /**
     * StatusCode : 200
     * AccessKeyId : STS.3p***dgagdasdg
     * AccessKeySecret : rpnwO9***tGdrddgsR2YrTtI
     * SecurityToken : CAES+wMIARKAAZhjH0EUOIhJMQBMjRywXq7MQ/cjLYg80Aho1ek0Jm63XMhr9Oc5s˙∂˙∂3qaPer8p1YaX1NTDiCFZWFkvlHf1pQhuxfKBc+mRR9KAbHUefqH+rdjZqjTF7p2m1wJXP8S6k+G2MpHrUe6TYBkJ43GhhTVFMuM3BZajY3VjZWOXBIODRIR1FKZjIiEjMzMzE0MjY0NzM5MTE4NjkxMSoLY2xpZGSSDgSDGAGESGTETqOio6c2RrLWRlbW8vKgoUYWNzOm9zczoqOio6c2RrLWRlbW9KEDExNDg5MzAxMDcyNDY4MThSBTI2ODQyWg9Bc3N1bWVkUm9sZVVzZXJgAGoSMzMzMTQyNjQ3MzkxMTg2OTExcglzZGstZGVtbzI=
     * Expiration : 2015-12-12T07:49:09Z
     * ErrorCode : InvalidAccessKeyId.NotFound
     * ErrorMessage : Specified access key is not found.
     * <p>
     * StatusCode：表示获取Token的状态，获取成功时，返回值是200。
     * AccessKeyId：表示Android/iOS应用初始化OSSClient获取的 AccessKeyId。
     * AccessKeySecret：表示Android/iOS应用初始化OSSClient获取AccessKeySecret。
     * SecurityToken：表示Android/iOS应用初始化的Token。
     * Expiration：表示该Token失效的时间。主要在Android SDK会自动判断是否失效，自动获取Token。
     * 错误返回说明：
     * StatusCode：表示获取Token的状态，获取失败时，返回值是500。
     * ErrorCode：表示错误原因。
     * ErrorMessage：表示错误的具体信息描述。
     */

    public int StatusCode;
    public String AccessKeyId;
    public String AccessKeySecret;
    public String SecurityToken;
    public String Expiration;
    public String ErrorCode;
    public String ErrorMessage;
}