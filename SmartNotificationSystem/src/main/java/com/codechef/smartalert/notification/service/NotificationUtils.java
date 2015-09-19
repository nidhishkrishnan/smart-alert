package com.codechef.smartalert.notification.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationUtils {

	ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
	private void Notify(){
		Map<Long, Product>  productMap= NotificationSystem.getInstance().getProductMap();
		
		for(Entry<Long, Product> entry:productMap.entrySet()){
			entry.getValue().createNotifications();
		}
	}
	

	public void doNotify(){
		 service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				Notify();
			}
		},0, 30, TimeUnit.SECONDS);
	}
	
}
