package com.susu.googleplay.http;

public interface Url {
	//服务器主机
	String SERVER_HOST = "http://127.0.0.1:8090/";
	//图片前缀
	String IMAGE_PREFIX = SERVER_HOST +"image?name=";
	//home页接口
	String HOME = SERVER_HOST + "home?index=";
	//app页接口
	String APP = SERVER_HOST + "app?index=";
	//game页接口
	String GAME = SERVER_HOST + "game?index=";
	//subject页接口
	String SUBJECT = SERVER_HOST + "subject?index=";
	//recommend页接口
	String RECOMMEND = SERVER_HOST + "recommend?index=";
	//category页接口
	String CATEGORY = SERVER_HOST + "category?index=";
	//hot页接口
	String HOT = SERVER_HOST + "hot?index=";
	//详情页接口
	String DETAIL = SERVER_HOST + "detail?packageName=%1$s";//%1$d,%2$s
	//从头下载
	String DOWNLOAD = SERVER_HOST + "download?name=%1$s";
	//断点下载
	String BREAK_DOWNLOAD = SERVER_HOST + "download?name=%1$s&range=%2$d";
}
