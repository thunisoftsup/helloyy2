package com.thunisoft.sswy.mobile.util;

import java.util.Collection;

/**
 * 
 * @author wuxm
 *
 */
public class CollectionUtils {
	/**
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection coll) {
		return (coll == null || coll.isEmpty());
	}
	
	/**
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Collection coll) {
		return !isEmpty(coll);
	}
}
