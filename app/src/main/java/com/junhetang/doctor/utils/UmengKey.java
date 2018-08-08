package com.junhetang.doctor.utils;

/**
 * UmengKey 自定义事件key
 * Create at 2018/8/6 下午1:27 by mayakun
 */
public interface UmengKey {
    //登录注册
    String login_registe = "login_registe";
    String login_password = "login_password";
    String login_code = "login_code";
    //认证
    String auth_step1 = "auth_step1";
    String auth_step2 = "auth_step2";
    //首页
    String workroom_addpatient = "workroom_addpatient";
    String workroom_patient_history = "workroom_patient_history";
    String workroom_online = "workroom_online";
    String workroom_notice = "workroom_notice";
    String workroom_job_schedule = "workroom_job_schedule";
    String workroom_history_paper = "workroom_history_paper";
    String workroom_follow_paper = "workroom_follow_paper";
    String workroom_comm_paper = "workroom_comm_paper";
    String workroom_chatlist = "workroom_chatlist";
    String workroom_camera = "workroom_camera";
    String workroom_banner = "workroom_banner";
    String workroom_ask_paper = "workroom_ask_paper";
    //患者
    String recentlist_chat = "recentlist_chat";
    String patient_history = "patient_history";
    //首页-添加患者
    String personcard_add = "personcard_add";
    String personcard_share = "personcard_share";
    //患者中心
    String patientcenter_remark = "patientcenter_remark";
    String patientcenter_itemclick = "patientcenter_itemclick";
    String patientcenter_chat = "patientcenter_chat";
    //处方详情
    String paperdetail_again = "paperdetail_again";
    //在线开方
    String online_choosepatient = "online_choosepatient";
    String online_writepatient = "online_writepatient";
    String online_submit = "online_submit";
    String online_historypatient = "online_historypatient";
    String online_commpaper = "online_commpaper";
    String online_add_commpaper = "online_add_commpaper";
    //拍照开发
    String camera_choosepatient = "camera_choosepatient";
    String camera_writepatient = "camera_writepatient";
    String camera_historypatient = "camera_historypatient";
    String camera_submit = "camera_submit";
    //历史处方
    String historypaper_all = "historypaper_all";
    String historypaper_haspay = "historypaper_haspay";
    String historypaper_unpay = "historypaper_unpay";
    String historypaper_closed = "historypaper_closed";
    String historypaper_all_search = "historypaper_all_search";
    String historypaper_haspay_search = "historypaper_haspay_search";
    String historypaper_unpay_search = "historypaper_unpay_search";
    String historypaper_closed_search = "historypaper_closed_search";
    String historypaper_itemclick = "historypaper_itemclick";
    //常用处方
    String commpaper_save = "commpaper_save";
    //问诊单 随诊单【发送】
    String askpaper_send = "askpaper_send";
    String followpaper_send = "followpaper_send";
    String askpaper_send_chat = "askpaper_send_chat";
    String followpaper_send_chat = "followpaper_send_chat";







}
