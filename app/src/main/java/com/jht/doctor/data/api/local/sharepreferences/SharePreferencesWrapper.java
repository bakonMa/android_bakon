package com.jht.doctor.data.api.local.sharepreferences;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.SPConfig;

import java.util.Set;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
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
        return getString(key, null);
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

    public Action1<? super String> getStringAction(@NonNull String key) {
        return getStringAction(key, null);
    }

    public Action1<? super String> getStringAction(@NonNull String key, @NonNull String defaultValue) {
        Preference<String> stringPreference = mRXPreferences.getString(key, defaultValue);
        settingDefaultValueWhenNotExists(stringPreference, defaultValue);
        return stringPreference.asAction();
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

    public Action1<? super Boolean> getBooleanAction(@NonNull String key) {
        return getBooleanAction(key, false);
    }

    public Action1<? super Boolean> getBooleanAction(@NonNull String key, @NonNull Boolean defaultValue) {
        Preference<Boolean> booleanPreference = mRXPreferences.getBoolean(key, defaultValue);
        settingDefaultValueWhenNotExists(booleanPreference, defaultValue);
        return booleanPreference.asAction();
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

    public Action1<? super Float> getFloatAction(@NonNull String key) {
        return getFloatAction(key, 0f);
    }

    public Action1<? super Float> getFloatAction(@NonNull String key, @NonNull Float defaultValue) {
        Preference<Float> floatPreference = mRXPreferences.getFloat(key, defaultValue);
        settingDefaultValueWhenNotExists(floatPreference, defaultValue);
        return floatPreference.asAction();
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

    public Action1<? super Integer> getIntegerAction(@NonNull String key) {
        return getIntegerAction(key, 0);
    }

    public Action1<? super Integer> getIntegerAction(@NonNull String key, @NonNull Integer defaultValue) {
        Preference<Integer> integerPreference = mRXPreferences.getInteger(key, defaultValue);
        settingDefaultValueWhenNotExists(integerPreference, defaultValue);
        return integerPreference.asAction();
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

    public Action1<? super Long> getLongAction(@NonNull String key) {
        return getLongAction(key, 0l);
    }

    public Action1<? super Long> getLongAction(@NonNull String key, @NonNull Long defaultValue) {
        Preference<Long> longPreference = mRXPreferences.getLong(key, defaultValue);
        settingDefaultValueWhenNotExists(longPreference, defaultValue);
        return longPreference.asAction();
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

    public Action1<? super Set<String>> getStringSetAction(@NonNull String key) {
        return getStringSetAction(key, null);
    }

    public Action1<? super Set<String>> getStringSetAction(@NonNull String key, @Nullable Set<String> defaultValue) {
        Preference<Set<String>> setPreference = mRXPreferences.getStringSet(key, defaultValue);
        settingDefaultValueWhenNotExists(setPreference, defaultValue);
        return setPreference.asAction();
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

    public <T extends Enum<T>> Action1<? super T> getEnumAction(@NonNull String key, @NonNull Class<T> enumClass) {
        return getEnumAction(key, null, enumClass);
    }

    public <T extends Enum<T>> Action1<? super T> getEnumAction(@NonNull String key, @Nullable T defaultValue, @NonNull Class<T> enumClass) {
        Preference<T> tPreference = mRXPreferences.getEnum(key, defaultValue, enumClass);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asAction();
    }

    public <T extends Enum<T>> void setEnum(@NonNull String key, @Nullable T value, @NonNull Class<T> enumClass) {
        mRXPreferences.getEnum(key, enumClass).set(value);
    }


    //==================================Object==================================//
    public <T> T getObject(@NonNull String key, @NonNull Preference.Adapter<T> adapter) {
        return getObject(key, null, adapter);
    }

    public <T> T getObject(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Adapter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.get();
    }

    public <T> Observable<T> getObjectObservable(@NonNull String key, @NonNull Preference.Adapter<T> adapter) {
        return getObjectObservable(key, null, adapter);
    }

    public <T> Observable<T> getObjectObservable(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Adapter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asObservable();
    }

    public <T> Action1<? super T> getObjectAction(@NonNull String key, @NonNull Preference.Adapter<T> adapter) {
        return getObjectAction(key, null, adapter);
    }

    public <T> Action1<? super T> getObjectAction(@NonNull String key, @Nullable T defaultValue, @NonNull Preference.Adapter<T> adapter) {
        Preference<T> tPreference = mRXPreferences.getObject(key, defaultValue, adapter);
        settingDefaultValueWhenNotExists(tPreference, defaultValue);
        return tPreference.asAction();
    }

    public <T> void setObject(@NonNull String key, @Nullable T value, @NonNull Preference.Adapter<T> adapter) {
        mRXPreferences.getObject(key, adapter).set(value);
    }

    public static abstract class SharePreferencesAdapter<T> implements Preference.Adapter<T> {

        @Override
        public T get(@NonNull String key, @NonNull SharedPreferences preferences) {
            return str2Obj(preferences.getString(key, null));
        }

        @Override
        public void set(@NonNull String key, @NonNull T value, @NonNull SharedPreferences.Editor editor) {
            editor.putString(key, obj2Str(value)).apply();
        }

        public abstract String obj2Str(T t);

        public abstract T str2Obj(String str);

    }

}
