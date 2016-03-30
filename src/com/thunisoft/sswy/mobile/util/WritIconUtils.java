/*
 * Copyright 2001-2013 Thunisoft,Inc. All rights reserved.
 * Company: 北京紫光华宇软件股份有限公司
 */
package com.thunisoft.sswy.mobile.util;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.thunisoft.dzfy.mobile.R;

/**
 * @author qiw
 * @version 1.0, 2013-3-22
 */
@EBean(scope = Scope.Singleton)
public class WritIconUtils {

	/**
	 * 根据文件扩展名获得文书图标资源ID
	 * 
	 * @param path
	 *            文件绝对路径
	 * @return
	 */
	public int getWritIconResId(String path) {
		int resId = R.drawable.writ_word;
		if (path != null && (path.endsWith("pdf") || path.endsWith("PDF"))) {
			resId = R.drawable.writ_pdf;
		}
		return resId;
	}

}
