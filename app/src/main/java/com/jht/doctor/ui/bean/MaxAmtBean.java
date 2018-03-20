package com.jht.doctor.ui.bean;

/**
 * Created by table on 2017/11/29.
 * description:
 */

public class MaxAmtBean {

    /**
     * configKey : string
     * configValue : string
     * desc : string
     * id : 0
     * isSystem : string
     */

    private String configKey;
    private String configValue;
    private String desc;
    private int id;
    private String isSystem;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
}
