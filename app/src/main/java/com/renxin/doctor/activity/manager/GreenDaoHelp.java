package com.renxin.doctor.activity.manager;

import android.text.TextUtils;

import com.renxin.doctor.activity.greendao.gen.DaoSession;
import com.renxin.doctor.activity.greendao.City;
import com.renxin.doctor.activity.greendao.gen.CityDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by mayakun on 2017/11/30
 * 和数据库交互相关方法
 */

public class GreenDaoHelp {

    /**
     * city
     * 查询所有【省】级城市
     */
    public static List<City> getAllProvince() {
        CityDao cityDao = GreenDaoManager.getInstance().getDaoSession().getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.ParentCode.eq('1')).build();
        return query.list();
    }

    /**
     * city
     * 查询所有【ParentCode】的城市
     */
    public static List<City> getCityByParentCode(int parentCode) {
        DaoSession daoSession = GreenDaoManager.getInstance().getDaoSession();
        CityDao cityDao = daoSession.getCityDao();
        daoSession.clear();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.ParentCode.eq(parentCode)).build();
        return query.list();
    }

    /**
     * city
     * 查询【CityId】的城市
     */
    public static City getCityById(int cityId) {
        DaoSession daoSession = GreenDaoManager.getInstance().getDaoSession();
        daoSession.clear();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.CityId.eq(cityId)).build();
        return (City) query.unique();
    }

    /**
     * city
     * 查询【CityCode】的城市
     */
    public static City getCityByCode(String cityCode) {
        DaoSession daoSession = GreenDaoManager.getInstance().getDaoSession();
        daoSession.clear();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.CityCode.eq(cityCode)).build();
        return (City) query.unique();
    }

    /**
     * city
     * 查询【CityCode】的城市名称
     */
    public static String getCityNameByCode(String cityCode) {
        DaoSession daoSession = GreenDaoManager.getInstance().getDaoSession();
        daoSession.clear();
        CityDao cityDao = daoSession.getCityDao();
        Query query = cityDao.queryBuilder()
                .where(CityDao.Properties.CityCode.eq(cityCode)).build();
        City city = (City) query.unique();
        return city == null ? "" : city.getCity();
    }


    /**
     * city
     * 查询【CityCode】的 省-市-区
     */
    public static String getLongCityName(String provinceCode, String cityCode, String areaCode) {
        StringBuffer stringBuffer = new StringBuffer("");
        //省
        String provinceName = getCityNameByCode(provinceCode);
        if (!TextUtils.isEmpty(provinceName)) {
            stringBuffer.append(provinceName).append("-");
        }
        //市
        String cityeName = getCityNameByCode(cityCode);
        if (!TextUtils.isEmpty(cityeName)) {
            stringBuffer.append(cityeName).append("-");
        }
        //区
        String areaName = getCityNameByCode(areaCode);
        if (!TextUtils.isEmpty(areaName)) {
            stringBuffer.append(areaName);
        }
        //返回 省-市-区
        return stringBuffer.toString();
    }
    /**
     * city
     * 查询【CityCode】的 省-市-区
     */
    public static String getLongCityName(String cityCode) {
        City qCity = getCityByCode(cityCode);
        if (qCity == null) {
            return "";
        }
        //是否是省
        if (1 == qCity.getParentCode()) {
            return qCity.getCity();
        } else {
            //市
            City sCity = getCityByCode(qCity.getCityCode() + "");
            if (sCity == null) {
                return qCity.getCity();
            }
            //是否是省
            if (1 == sCity.getParentCode()) {
                return sCity.getCity() + "-" + qCity.getCity();
            } else {
                //省
                City city = getCityByCode(sCity.getParentCode() + "");
                if (city == null) {
                    return sCity.getCity() + "-" + qCity.getCity();
                } else {
                    return city.getCity() + "-" + sCity.getCity() + "-" + qCity.getCity();
                }
            }
        }

    }


}
