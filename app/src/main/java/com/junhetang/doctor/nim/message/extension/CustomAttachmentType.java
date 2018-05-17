package com.junhetang.doctor.nim.message.extension;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    // 多端统一
    int Guess = 1;
    int SnapChat = 2;
    int Sticker = 3;
    int RTS = 4;
    int RedPacket = 5;
    int OpenedRedPacket = 6;

    int MESSAGE_PAY_OK = 7;//第一条患者信息消息
    int MESSAGE_SYSTEM = 8;//自定义系统消息
    int MESSAGE_ASK_PAPER = 9;//问诊单
    int MESSAGE_FOllOW_PAPER = 10;//随诊单
    int MESSAGE_DOC_OPENPAPER = 11;//医生开方完成提示
    int MESSAGE_ClOSE = 12;//结束聊天提示
}
