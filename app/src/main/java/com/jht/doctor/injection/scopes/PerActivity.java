package com.jht.doctor.injection.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
