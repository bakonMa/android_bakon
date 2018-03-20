package com.jht.doctor.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by mayakun on 2017/11/30
 */
@Entity(
        nameInDb = "CitiesInfoTable",
        createInDb = false
)
public class City {
    @Id
    @OrderBy
    @Property(nameInDb = "cityId")
    private int cityId;
    @Property(nameInDb = "cityCode")
    private int cityCode;
    @Property(nameInDb = "city")
    private String city;//城市名
    @Property(nameInDb = "parentCode")
    private int parentCode;

    @Generated(hash = 750791287)
    public City() {
    }

    @Generated(hash = 908880740)
    public City(int cityId, int cityCode, String city, int parentCode) {
        this.cityId = cityId;
        this.cityCode = cityCode;
        this.city = city;
        this.parentCode = parentCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }
}
