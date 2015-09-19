package com.codechef.smartalert.notification.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.codechef.smartalert.notification.service.NotificationCondition.Condition;

public class Product {

	private final long productId;
	private final ConcurrentHashMap<String, String> attributeMap;
	private final ConcurrentHashMap<String, String> newAttributeMap;
	private final ConcurrentHashMap<String,Map<Integer,UserCondition>> notifySubscriberMap;

	protected Product(long productId){
		this.productId = productId;
		attributeMap = new ConcurrentHashMap<String, String>();
		newAttributeMap = new ConcurrentHashMap<String, String>();
		notifySubscriberMap = new ConcurrentHashMap<String,Map<Integer,UserCondition>>();
	}

	public long getProductId() {
		return productId;
	}

	public ConcurrentHashMap<String,Map<Integer,UserCondition>> getNotifySubscriberMap() {
		return notifySubscriberMap;
	}

	public void putAttribute(String name,String value){
		newAttributeMap.put(name, value);
	}
	
	public void createNotifications(){
		for(Entry<String,String> entry:newAttributeMap.entrySet()){
			String attName = entry.getKey();
			String attVal = entry.getValue();
			String prevVal = attributeMap.get(attName);
			if(notifySubscriberMap.containsKey(attName)){
					if(prevVal!=attVal){
						for(Entry<Integer,UserCondition> entry3:notifySubscriberMap.get(attName).entrySet()){
								entry3.getValue().subscriber.getNotifications().add("Change in "+attName+", old value : "+prevVal+", old value : "+attVal);
								System.out.println("Change in "+attName);
						}
				}
			}
			attributeMap.put(attName, attVal);
		}
		newAttributeMap.clear();
	}
	
	public static class UserCondition{
		UserCondition(Subscriber subscriber,Condition condition){
			this.subscriber = subscriber;
			this.condition = condition;
		}
		Subscriber subscriber;
		Condition condition;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", attributeMap="
				+ attributeMap + ", newAttributeMap=" + newAttributeMap
				+ ", notifySubscriberMap=" + notifySubscriberMap + "]";
	}
}
