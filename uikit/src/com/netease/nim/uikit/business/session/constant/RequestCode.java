package com.netease.nim.uikit.business.session.constant;

public class RequestCode {
    public final static int CAPTURE_VIDEO = 1;// 拍摄视频
    public final static int GET_LOCAL_VIDEO = 2;// 选择视频
    public final static int GET_LOCAL_FILE = 3; // 选择文件
    public final static int PICK_IMAGE = 4;
    public final static int PICKER_IMAGE_PREVIEW = 5;
    public final static int PREVIEW_IMAGE_FROM_CAMERA = 6;
    public final static int GET_LOCAL_IMAGE = 7;// 相册
    public final static int SEND_ACK_MESSAGE = 8; // 发送需要已读回执的消息
    //jth使用
    public final static int SEND_ASKPAPER = 10; //发送问诊单-进入h5
    public final static int SEND_ASKPAPER_INFO = 11; //点击问诊单-进入h5
    public final static int SEND_FOLLOWPAPER = 12; //发送随诊单-进入h5
    public final static int SEND_FOLLOWPAPER_INFO = 13; //点击随诊单-进入h5
    public final static int SEND_COMM_MESSAGE = 14; //点击常用语
    public final static int SEND_OPENPAPER_CAMERA = 15; //发送拍照开方
    public final static int SEND_OPENPAPER_ONLINE = 16; //发送在线开方
}
