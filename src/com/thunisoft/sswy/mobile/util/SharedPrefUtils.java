package com.thunisoft.sswy.mobile.util;

import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * 简单存储
 * @author lulg
 *
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SharedPrefUtils {
    
    @DefaultLong(0L)
    long courtUpdate();

}
