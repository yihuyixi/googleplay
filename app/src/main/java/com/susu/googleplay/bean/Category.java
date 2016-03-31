package com.susu.googleplay.bean;

import java.util.ArrayList;

public class Category {
	private String title;
	private ArrayList<CategoryInfo> infos;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<CategoryInfo> getInfos() {
		return infos;
	}
	public void setInfos(ArrayList<CategoryInfo> infos) {
		this.infos = infos;
	}
	
	
	
}
