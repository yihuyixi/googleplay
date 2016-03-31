package com.susu.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池管理
 * @author Administrator
 *
 */
public class ThreadPoolManager {
	private static ThreadPoolManager mInstance = new ThreadPoolManager();
	private int corePoolSize;//核心线程池的数量，同时能执行的线程数量
	
	private int maximumPoolSize = 5;//最大线程池的数量
	private int keepAliveTime = 1;//存活的时间
	private TimeUnit unit = TimeUnit.HOURS;//时间单位
	
	private static ThreadPoolExecutor executor;//线程池执行器
	
	private ThreadPoolManager(){
		corePoolSize = Runtime.getRuntime().availableProcessors()*2 + 1;//可用处理器核数*2+1
		executor = new ThreadPoolExecutor(
				corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				unit, 
				new LinkedBlockingDeque<Runnable>(), //无限容量的缓冲队列
				Executors.defaultThreadFactory(), //线程创建工厂
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	public static ThreadPoolManager getInstance(){
		return mInstance;
	}
	/**
	 * 执行任务
	 * @param runnable
	 */
	public void execute(Runnable runnable){
		if(runnable!=null){
			executor.execute(runnable);
		}
	}
	/**
	 * 移除线程
	 * @param runnable
	 */
	public void remove(Runnable runnable){
		if(runnable!=null){
			executor.remove(runnable);
		}
	}
}
