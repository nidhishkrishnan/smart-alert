package com.codechef.smartalert.notification.service;

import java.util.Timer;
import java.util.TimerTask;

public class StartUp {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		FileProcessor processor = new FileProcessor("F:\\Folder1");
		processor.doProcess();

		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				Subscriber subscriber = new Subscriber(1);
				NotificationCondition condition = new NotificationCondition();
				condition.setProductId(13579);
				condition.setAttributName("price");
				//System.out.println("b: "+NotificationSystem.getInstance().registerSubscriberForNotification(condition, subscriber));
			}
		}, 10000);
		NotificationSystem.getInstance().startNotify();
	}

}
