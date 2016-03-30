package com.thunisoft.sswy.mobile.pojo;

import org.json.JSONArray;

public class JsonBean {
	
	private String title;
	
	private JSONArray ablockContent;

	public JsonBean() {
		super();
	}

	public JsonBean(String title, JSONArray ablockContent) {
		super();
		this.title = title;
		this.ablockContent = ablockContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONArray getAblockContent() {
		return ablockContent;
	}

	public void setAblockContent(JSONArray ablockContent) {
		this.ablockContent = ablockContent;
	}
}
