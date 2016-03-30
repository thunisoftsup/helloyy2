package com.thunisoft.sswy.mobile.cache;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;

import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.DzfyCryptanalysis;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EBean(scope = Scope.Singleton)
public class LoginCache {
    @Pref
    ConfigUtils_ configUtils;
    @Bean
    NetUtils netUtils;
    public static final int LOGIN_TYPE_NOT_VERIFID = 1;// 未认证
    public static final int LOGIN_TYPE_VERIFID = 2;// 实名认证
    //public static final int LOGIN_TYPE_LS_VERIFID = 3;// 律师认证
    public static final int LOGIN_TYPE_LS_VERIFID = 4;// 律协认证
    public static final int LOGIN_TYPE_NORMAL=5;
    public static final int LOGIN_TYPE_LAYWER=6;
    
    public static final int LOGIN_TYPE_LAYWER_PHONE=7;
    public static final int LOHIN_TYPE_LAYWER_TOKEN=8; 

    private boolean logined;
    private int loginType;
    private boolean isGetPhoneCode=false;

    private String userName;
    private String password;
    private String xm;
    private String zjhm;
    private String zjLx;
    private String phone;
    private String userId;
    
    private int userType;

	private String sessionId;
	//手机验证码
	private String phoneCode;
    
    private boolean sfrzbg;//是否认证变更
    private JSONArray openModule;
    
    private String lxUrl;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final WriteLock wLock = lock.writeLock();
    private final ReadLock rLock = lock.readLock();
    
    public JSONArray getOpenModule() {
    	 rLock.lock();
         try {
             if (openModule == null) {
            	 try {
            		 String openModuleStr = configUtils.getSharedPreferences().getString("openModule", null);
            		 if (openModuleStr != null) {
            			 openModule = new JSONArray(openModuleStr);
            		 }
            	 } catch (JSONException e) {
            		 e.printStackTrace();
            	 }
             }
             return openModule;
         } finally {
             rLock.unlock();
         }
	}

	public void setOpenModule(JSONArray openModule) {
		wLock.lock();
		try {
            configUtils.getSharedPreferences().edit().putString("openModule", openModule.toString()).commit();
            this.openModule = openModule;
        } finally {
            wLock.unlock();
        }
	}
    
    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
        configUtils.getSharedPreferences().edit().putBoolean("logined", logined).commit();
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        configUtils.getSharedPreferences().edit().putInt("loginType", loginType).commit();
        this.loginType = loginType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        configUtils.getSharedPreferences().edit().putString("userName", userName).commit();
        this.userName = userName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
    	String phoneTemp;
    	try {
    		phoneTemp = DzfyCryptanalysis.encrypt(phone);
		} catch (Exception e) {
			phoneTemp = phone;
		}
        configUtils.getSharedPreferences().edit().putString("phone", phoneTemp).commit();
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    	String pwd;
    	try {
    		pwd = DzfyCryptanalysis.encrypt(password);
		} catch (Exception e) {
			pwd = password;
		}
        configUtils.getSharedPreferences().edit().putString("password", pwd).commit();
        this.password = password;
    }
    

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		configUtils.getSharedPreferences().edit().putString("userId", userId).commit();
		this.userId = userId;
	}


    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        if (zjhm == null || zjhm.equals("null")) {
            zjhm = "";
        }
        String zjhmTemp;
    	try {
    		zjhmTemp = DzfyCryptanalysis.encrypt(zjhm);
		} catch (Exception e) {
			zjhmTemp = zjhm;
		}
        configUtils.getSharedPreferences().edit().putString("zjhm", zjhmTemp).commit();
        this.zjhm = zjhm;
    }
    
    public String getZjLx() {
    	return zjLx;
    }
    
    public void setZjLx(String zjLx) {
    	if (zjLx == null || zjLx.equals("null")) {
    		zjLx = "";
    	}
    	configUtils.getSharedPreferences().edit().putString("zjLx", zjLx).commit();
    	this.zjLx = zjLx;
    }

    public boolean isSfrzbg() {
        return sfrzbg;
    }

    public void setSfrzbg(boolean sfrzbg) {
        this.sfrzbg = sfrzbg;
    }

    public boolean isLsrz() {
        return loginType == LOGIN_TYPE_LS_VERIFID;
    }

    public boolean isSmrz() {
        return loginType == LOGIN_TYPE_VERIFID;
    }

    public boolean isNotVerified() {
        return loginType == LOGIN_TYPE_NOT_VERIFID;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        configUtils.getSharedPreferences().edit().putString("xm", xm).commit();
        this.xm = xm;
    }
    
    
	public String getLxUrl() {
		if (StringUtils.isNotBlank(lxUrl) && !lxUrl.endsWith("/")) {
			lxUrl = lxUrl + "/";
		}
		return lxUrl;
	}

	public void setLxUrl(String lxUrl) {
		this.lxUrl = lxUrl;
	}

    public void clear() {
        setLogined(false);
        setLoginType(0);
        setUserName("");
        setPassword("");
        setZjhm("");
        setXm("");
    }

    public void setSessionId(String sessionId) {
        wLock.lock();
        try {
            this.sessionId = sessionId;
        } finally {
            wLock.unlock();
        }

    }

    public String getSessionId() {
        rLock.lock();
        try {
            return sessionId;
        } finally {
            rLock.unlock();
        }
    }

    public void initLoginCatch() {
        setLogined(configUtils.getSharedPreferences().getBoolean("logined", logined));
        setLoginType(configUtils.getSharedPreferences().getInt("loginType", loginType));
        setUserName(configUtils.getSharedPreferences().getString("userName", userName));
        
        String pwdTemp;
    	try {
    		pwdTemp = DzfyCryptanalysis.decrypt(configUtils.getSharedPreferences().getString("password", password));
		} catch (Exception e) {
			pwdTemp = password;
		}
        setPassword(pwdTemp);
        setUserId(configUtils.getSharedPreferences().getString("userId", userId));
        
        String phoneTemp;
    	try {
    		phoneTemp = DzfyCryptanalysis.decrypt(configUtils.getSharedPreferences().getString("phone", phone));
		} catch (Exception e) {
			phoneTemp = phone;
		}
        setPhone(phoneTemp);
        
        String zjhmTemp;
    	try {
    		zjhmTemp = DzfyCryptanalysis.decrypt(configUtils.getSharedPreferences().getString("zjhm", zjhm));
		} catch (Exception e) {
			zjhmTemp = zjhm;
		}
        setZjhm(zjhmTemp);
        setZjLx(configUtils.getSharedPreferences().getString("zjLx", zjLx));
        setXm(configUtils.getSharedPreferences().getString("xm", xm));
    }

    public void setAddressMD5(String md5) {
        configUtils.getSharedPreferences().edit().putString("address_md5", md5).commit();
    }
    
    public String getAddressMD5() {
        return configUtils.getSharedPreferences().getString("address_md5", "");
    }
    
    public void setPhoneCode(String phoneCode){
    	this.phoneCode = phoneCode;
    }
    
    public String getPhoneCode(){
    	return this.phoneCode;
    }
    
    public void setIsGetPhoneCode(boolean isGetPhoneCode){
    	this.isGetPhoneCode = isGetPhoneCode;
    }
    
    public boolean getIsGetPhoneCode(){
    	return this.isGetPhoneCode;
    }

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
    
    
}
