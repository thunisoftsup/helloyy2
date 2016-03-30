package com.thunisoft.sswy.mobile.util;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface ConfigUtils {
//	http://172.16.30.100:8080/susong51（dzfy外网）
//		http://172.16.30.99:8080/dzfy-sfgk（dzfy内网）
//	http://172.16.192.236:8080/susong51/mobile/getSfList.htm
//	 @DefaultString("www.sswy.com")
//	 @DefaultString("172.16.192.236")
	 @DefaultString("172.16.30.100")
//	@DefaultString("172.16.203.214")
//	@DefaultString("susong51.thunisoft.com")
	    String serverHost();
	    
//	    @DefaultInt(9090)
	   @DefaultInt(8080)
//	@DefaultInt(9456)
	    int serverPort();
	    
//	    @DefaultString("susong51")
	    @DefaultString("dzfy-ww")
//	    @DefaultString("")
	    String context();
	    
	    @DefaultLong(9458)
	    long gdtpXssj();
	    
	    @DefaultInt(2)
	    int ydtpVersion();
	    
}
