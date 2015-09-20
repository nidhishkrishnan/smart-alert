package configurableNotificationSystem;

import java.util.HashMap;

public class NotificationUtils {

	
	private static HashMap<Integer, Subscriber1> subScribersMap = new HashMap<Integer, Subscriber1>();

	
	static void populateSubscriberData(){
		/*  All active observers in the db will be populated along with their callback   */
//		ArrayList<Subscriber1>arrayList = DbPersist.getInstance().getSubScribers();
//		
//		for(Subscriber1 subscriber1:arrayList){
//			subScribersMap.put(subscriber1.getId(), subscriber1);
//		}
//		System.out.println(subScribersMap);
	}
	
	public static void addSubscribers(String userName){
		Subscriber1 subscriber1 = DbPersist.getInstance().getSubscriber(userName);
		subScribersMap.put(subscriber1.getId(), subscriber1);
	}
	
	public static void addSubscribers(Subscriber1 subscriber){
		subScribersMap.put(subscriber.getId(), subscriber);
	}
	
//	ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
//	private void Notify(){
//		Map<Long, Product>  productMap= NotificationSystem.getInstance().getProductMap();
//		
//		for(Entry<Long, Product> entry:productMap.entrySet()){
//			entry.getValue().createNotifications();
//		}
//	}
//	
//
//	public void doNotify(){
//		 service.scheduleAtFixedRate(new Runnable() {
//			
//			@Override
//			public void run() {
//				Notify();
//			}
//		},0, 30, TimeUnit.SECONDS);
//	}

}

