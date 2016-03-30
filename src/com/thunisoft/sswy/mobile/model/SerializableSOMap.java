package com.thunisoft.sswy.mobile.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 封装Map，实现序列化，用于Intent传递Map参数<br>
 * Map[String, Object]
 * @author gewx
 *
 */
public class SerializableSOMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5252137339213673672L;
	
	private Map<String, Object> map;

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
