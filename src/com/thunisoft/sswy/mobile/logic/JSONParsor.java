package com.thunisoft.sswy.mobile.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.thunisoft.sswy.mobile.util.AppSecretUtil;

public abstract class JSONParsor<E> {
    public abstract E parseToBean(String result);

    public abstract E getResponse(String url, List<NameValuePair> params);

    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    public JSONParsor<E> clearParams() {
        params.clear();
        return this;
    }
    
    public List<NameValuePair> getParams() {
        return params;
    }
    
    public List<NameValuePair> getParams(String defaultKey, String defaultValue) {
        params.clear();
        params.add(new BasicNameValuePair(defaultKey, defaultValue));
        return params;
    }
    
    public JSONParsor<E> addParam(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        return this;
    }
    
    public JSONParsor<E> addSecretParam(String key, String value) {
        params.add(new BasicNameValuePair(key,AppSecretUtil.encodeAppString(value) ));
        return this;
    }
    
    public List<NameValuePair> getEmptyParams() {
        params.clear();
        return params;
    }

}
