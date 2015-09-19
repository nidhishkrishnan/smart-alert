package com.codechef.smartalert.notification.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.codechef.smartalert.notification.service.Product.UserCondition;

public class NotificationSystem {
	private static final Object lock = new Object();
	private static volatile NotificationSystem notificationSystem;
	private final Map<Long, Product> productMap;
	private final Map<Integer, Subscriber> subscriberMap;
	
	public static NotificationSystem getInstance(){
		NotificationSystem system =notificationSystem;
		if(system==null){
			synchronized (lock) {
				if(system==null){
					system=new NotificationSystem();
					notificationSystem=system;
				}
			}
		}
		return system;
	}
	
	private NotificationSystem (){
		 productMap = new HashMap<Long, Product>();
		 subscriberMap = new HashMap<Integer,Subscriber>();
	}

	protected Map<Long, Product> getProductMap() {
		return productMap;
	}	

	public boolean addSubscriber(int subscriberId){
		Subscriber subscriber = new Subscriber(subscriberId);
		subscriberMap.put(subscriberId, subscriber);
		return true;
	}

	public boolean registerSubscriberForNotification(NotificationCondition condition,int subscriberId){
		if(subscriberId<=0||condition==null){return false;}
		Subscriber subscriber = subscriberMap.get(subscriberId);
		if(subscriber==null){return false;}
		long prdId = condition.getProductId();
		if(productMap.containsKey(prdId)){
			Product product = productMap.get(prdId);
			if(!product.getNotifySubscriberMap().containsKey(condition.getAttributName())){
				product.getNotifySubscriberMap().put(condition.getAttributName(),new HashMap<Integer, Product.UserCondition>());
			}
			Map<Integer, Product.UserCondition> map1 = product.getNotifySubscriberMap().get(condition.getAttributName());
			map1.put(subscriber.getSubscriberId(), new UserCondition(subscriber, condition.getCondition()));
			subscriberMap.get(subscriber.getSubscriberId()).getConditions().add(condition);
			return true;
		}
		return false;
	}
	
	public void startNotify(){
		new NotificationUtils().doNotify();
	}
	
	public ArrayList<String> getNoti(int id){
		Subscriber subscriber = subscriberMap.get(id);
		if(subscriber==null)return null;
		ArrayList<String> list =new ArrayList<String>(subscriber.getNotifications());  
		subscriber.getNotifications().clear();
		return subscriber!=null?list:null;
	}
}
