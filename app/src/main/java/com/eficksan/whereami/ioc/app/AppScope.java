package com.eficksan.whereami.ioc.app;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Aleksei Ivshin
 * on 28.08.2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
