package com.junhetang.doctor.data.localdata;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.SPConfig;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail: RxSharedPreferences 暫時沒有使用
 */
public final class SharePreferencesWrapper {

    private final DocApplication mApplication;

    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;
    private final RxSharedPreferences mRXPreferences;

    public SharePreferencesWrapper(DocApplication application , String spName) {
        mApplication = application;
        mSharedPreferences = mApplication.getSharedPreferences(spName , SPConfig.GENERAL_SP_MODE);
        mEditor = mSharedPreferences.edit();
        mRXPreferences = RxSharedPreferences.create(mSharedPreferences);
    }

    public SharedPreferences getSharedPreferencesInstance() {
        return mSharedPreferences;
    }

    public void remove(@NonNull String key) {
        mEditor.remove(key).apply();
    }

    private <T> void settingDefaultValueWhenNotExists(Preference<T> preference, T value) {
        if (!preference.isSet()) {
            preference.set(value);
        }
    }

    //==================================String==================================//
    public String getString(@NonNull String key) {
        return getString(key, "");
    }

    public String getString(@NonNull String key, @NonNull String defaultValue) {
        Preference<String> stringPreference = mRXPreferences.getString(key, defaultValue);
        settingDefaultValueWhenNotExists(stringPreference, defaultValue);
        return stringPreference.get();
    }

    public Observable<String> getStringObservable(@NonNull String key) {
        return getStringObservable(key, null);
    }

    public Observable<String> getStringObservable(@NonNull String key, @NonNull String defaultValue) {
        Preference<String> stringPreference = mRXPreferences.getString(key, defaultValue);
        settingDefaultValueWhenNotExists(stringPreference, defaultValue);
        return stringPreference.asObservable();
    }

    public Consumer<? super String> getStringConsumer(@NonNull String key) {
        return getStringConsumer(key, null);
    }

    public Consumer<? super String> getStringConsumer(@NonNull String key, @NonNull String defaultValue) {
        Preference<String> stringPreference = mRXPreferences.getString(key, defaultValue);
        settingDefaultValueWhenNotExists(stringPreference, defaultValue);
        return stringPreference.asConsumer();
    }

    public void setString(@NonNull String key, @Nullable String value) {
        mRXPreferences.getString(key).set(value);
    }

    //==================================Boolean==================================//
    public Boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    public Boolean getBoolean(@NonNull String key, @NonNull Boolean defaultValue) {
        Preference<Boolean> booleanPreference = mRXPreferences.getBoolean(key, defaultValue);
        settingDefaultValueWhenNotExists(booleanPreference, defaultValue);
        return booleanPreference.get();
    }

    public Observable<Boolean> getBooleanObservable(@NonNull String key) {
        return getBooleanObservable(key, false);
    }

    public Observable<Boolean> getBooleanObservable(@NonNull String key, @NonNull Boolean defaultValue) {
        Preference<Boolean> booleanPreference = mRXPreferences.getBoolean(key, defaultValue);
        settingDefaultValueWhenNotExists(booleanPreference, defaultValue);
        return booleanPreference.asObservable();
    }

    public Consumer<? super Boolean> getBooleanConsumer(@NonNull String key) {
        return getBooleanConsumer(key, false);
    }

    public Consumer<? super Boolean> getBooleanConsumer(@NonNull String key, @NonNull Boolean defaultValue) {
        Preference<Boolean> booleanPreference = mRXPreferences.getBoolean(key, defaultValue);
        settingDefaultValueWhenNotExists(booleanPreference, defaultValue);
        return booleanPreference.asConsumer();
    }

    public void setBoolean(@NonNull String key, @Nullable Boolean value) {
        mRXPreferences.getBoolean(key).set(value);
    }


    //==================================Float==================================//
    public Float getFloat(@NonNull String key) {
        return getFloat(key, 0f);
    }

    public Float getFloat(@NonNull String key, @NonNull Float defaultValue) {
        Preference<Float> floatPreference = mRXPreferences.getFloat(key, defaultValue);
        settingDefaultValueWhenNotExists(floatPreference, defaultValue);
        return floatPreference.get();
    }

    public Observable<Float> getFloatObservable(@NonNull String key) {
        return getFloatObservable(key, 0f);
    }

    public Observable<Float> getFloatObservable(@NonNull String key, @NonNull Float defaultValue) {
        Preference<Float> floatPreference = mRXPreferences.getFloat(key, defaultValue);
        settingDefaultValueWhenNotExists(floatPreference, defaultValue);
        return floatPreference.asObservable();
    }

    public Consumer<? super Float> getFloatConsumer(@NonNull String key) {
        return getFloatConsumer(key, 0f);
    }

    public Consumer<? super Float> getFloatConsumer(@NonNull String key, @NonNull Float defaultValue) {
        Preference<Float> floatPreference = mRXPreferences.getFloat(key, defaultValue);
        settingDefaultValueWhenNotExists(floatPreference, defaultValue);
        return floatPreference.asConsumer();
    }

    public void setFloat(@NonNull String key, @Nullable Float value) {
        mRXPreferences.getFloat(key).set(value);
    }


    //==================================Integer==================================//
    public Integer getInteger(@NonNull String key) {
        return getInteger(key, 0);
    }

    public Integer getInteger(@NonNull String key, @NonNull Integer defaultValue) {
        Preference<Integer> integerPreference = mRXPreferences.getInteger(key, defaultValue);
        settingDefaultValueWhenNotExists(integerPreference, defaultValue);
        return integerPreference.get();
    }

    public Observable<Integer> getIntegerObservable(@NonNull String key) {
        return getIntegerObservable(key, 0);
    }

    public Observable<Integer> getIntegerObservable(@NonNull String key, @NonNull Integer defaultValue) {
        Preference<Integer> integerPreference = mRXPreferences.getInteger(key, defaultValue);
        settingDefaultValueWhenNotExists(integerPreference, defaultValue);
        return integerPreference.asObservable();
    }

    public Consumer<? super Integer> getIntegerConsumer(@NonNull String key) {
        return getIntegerConsumer(key, 0);
    }

    public Consumer<? super Integer> getIntegerConsumer(@NonNull String key, @NonNull Integer defaultValue) {
        Preference<Integer> integerPreference = mRXPreferences.getInteger(key, defaultValue);
        settingDefaultValueWhenNotExists(integerPreference, defaultValue);
        return integerPreference.asConsumer();
    }

    public void setInteger(@NonNull String key, @Nullable Integer value) {
        mRXPreferences.getInteger(key).set(value);
    }


    //==================================Long==================================//
    public Long getLong(@NonNull String key) {
        return getLong(key, 0l);
    }

    public Long getLong(@NonNull String key, @NonNull Long defaultValue) {
        Preference<Long> longPreference = mRXPreferences.getLong(key, defaultValue);
        settingDefaultValueWhenNotExists(longPreference, defaultValue);
        return longPreference.get();
    }

    public Observable<Long> getLongObservable(@NonNull String key) {
        return getLongObservable(key, 0l);
    }

    public Observable<Long> getLongObservable(@NonNull String key, @NonNull Long defaultValue) {
        Preference<Long> longPreference = mRXPreferences.getLong(key, defaultValue);
        settingDefaultValueWhenNotExists(longPreference, defaultValue);
        return longPreference.asObservable();
    }

    public Consumer<? super Long> getLongConsumer(@NonNull String key) {
        return getLongConsumer(key, 0l);
    }

    public Consumer<? super Long> getLongConsumer(@NonNull String key, @NonNull Long defaultValue) {
        Preference<Long> longPreference = mRXPreferences.getLong(key, defaultValue);
        settingDefaultValueWhenNotExists(longPreference, defaultValue);
        return longPreference.asConsumer();
    }

    public void setLong(@NonNull String key, @Nullable Long value) {
        mRXPreferences.getLong(key).set(value);
    }


    //==================================StringSet==================================//
    public Set<String> getStringSet(@NonNull String key) {
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defaultValue) {
        Preference<Set<String>> setPreference = mRXPreferences.getStringSet(key, defaultValue);
        settingDefaultValueWhenNotExists(setPreference, defaultValue);
        return setPreference.get();
    }

    public Observable<Set<String>> getStringSetObservable(@NonNull String key) {
        return getStringSetObservable(key, null);
    }

    public Observable<Set<String>> getStringSetObservable(@NonNull String key, @Nullable Set<String> defaultValue) {
        Preference<Set<String>> setPreference = mRXPreferences.getStringSet(key, defaultValue);
        settingDefaultValueWhenNotExists(setPreference, defaultValue);
        return setPreference.asObservable();
    }

    public Consumer<? super Set<String>> getStringSetConsumer(@NonNull String key) {
        return getStringSetConsumer(key, null);
    }

    public Consumer<? super Set<String>> getStringSetConsumer(@NonNull String key, @Nullable Set<String> defaultValue) {
        Preference<Set<String>> setPreference = mRXPreferences.getStringSet(key, defaultValue);
        settingDefaultValueWhenNotExists(setPreference, defaultValue);
        return setPreference.asConsumer();
    }

    public void setStringSet(@NonNull String key, @Nullable Set<String> value) {
        mRXPreferences.getStringSet(key).set(value);
    }


    //==================================Enum==================================//
    public <T extends Enum<T>> T getEnum(@NonNull String key, @NonNull Class<T> enumClass) {
        return getEnum(key, null, enumClass);
    }

    public <T extends Enum<T>> T getEnum(@NonNull String key, @Nullable T defaultValue, @NonNull Class<T> enumClass) {
        Preference<T> tPreference = mRXPreferences.getEnum(key, defaultValue, enumClass);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.get();
    }

    public <T extends Enum<T>> Observable<T> getEnumObservable(@NonNull String key, @NonNull Class<T> enumClass) {
        return getEnumObservable(key, null, enumClass);
    }

    public <T extends Enum<T>> Observable<T> getEnumObservable(@NonNull String key, @Nullable T defaultValue, @NonNull Class<T> enumClass) {
        Preference<T> tPreference = mRXPreferences.getEnum(key, defaultValue, enumClass);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asObservable();
    }

    public <T extends Enum<T>> Consumer<? super T> getEnumConsumer(@NonNull String key, @NonNull Class<T> enumClass) {
        return getEnumConsumer(key, null, enumClass);
    }

    public <T extends Enum<T>> Consumer<? super T> getEnumConsumer(@NonNull String key, @Nullable T defaultValue, @NonNull Class<T> enumClass) {
        Preference<T> tPreference = mRXPreferences.getEnum(key, defaultValue, enumClass);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asConsumer();
    }

    //==================================Object==================================//
    public <T> T getObject(@NonNull String key, @NonNull Preference.Converter<T> adapter) {
        return getObject(key, null, adapter);
    }

    public <T> T getObject(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Converter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.get();
    }

    public <T> Observable<T> getObjectObservable(@NonNull String key, @NonNull Preference.Converter<T> adapter) {
        return getObjectObservable(key, null, adapter);
    }

    public <T> Observable<T> getObjectObservable(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Converter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asObservable();
    }

    public <T> Consumer<? super T> getObjectConsumer(@NonNull String key, @NonNull Preference.Converter<T> adapter) {
        return getObjectConsumer(key, null, adapter);
    }

    public <T> Consumer<? super T> getObjectConsumer(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Converter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asConsumer();
    }

}
