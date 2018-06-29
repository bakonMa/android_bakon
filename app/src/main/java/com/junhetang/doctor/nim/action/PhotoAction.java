package com.junhetang.doctor.nim.action;

import android.Manifest;

import com.junhetang.doctor.R;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.Observer;

/**
 * PhotoAction 照片，拍照
 * Create at 2018/4/18 下午6:27 by mayakun
 */
public class PhotoAction extends PickImageAction {
    private int type;

    public PhotoAction(int type) {
        super(type == 0 ? R.drawable.chat_photo : R.drawable.chat_camera,
                type == 0 ? R.string.input_panel_image : R.string.input_panel_camera, true);
        this.type = type;
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
        } else {
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        }
        sendMessage(message);
    }


    @Override
    public void onClick() {
        //拍照权限
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
                            showSelector(getTitleId(), requestCode, false, tempFile(), type);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }

}
