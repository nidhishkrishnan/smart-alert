package configurableNotificationSystem;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import configurableNotificationSystem.DbPersist.ProductDetails;

public class NotificationSystem {
	private static final Object lock = new Object();
	private static volatile NotificationSystem notificationSystem;
	public static BlockingQueue<SubscriberNotificationMsg> toBeNotifiedBlockingQueue = new LinkedBlockingQueue<SubscriberNotificationMsg>();
	
	
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
		 NotificationUtils.populateSubscriberData();
		 new Thread(new NotifyThreadConsumer()).start();
	}

	public boolean addSubscriber(String userName,String email){
		return DbPersist.getInstance().addUser(userName,email);
	}

	public ArrayList<String> getNotification(int userId){
		return DbPersist.getInstance().getNotification(userId);
	}

	public void registerSubscriberForNotification(NotificationCondition condition,int subscriberId){
		if(subscriberId<=0||condition==null){return;}
		DbPersist.getInstance().regiseterUserForNotification(subscriberId, condition);
	}

	public ProductDetails productDetails(long id){
		ProductDetails details =DbPersist.getInstance().getProductDetails(id);
		System.out.println("Details : "+details);
		return details;
	}

	
	class NotifyThreadConsumer implements Runnable	{

		@Override
		public void run() {
			while(true){
				try {
					SubscriberNotificationMsg notificationMsg = toBeNotifiedBlockingQueue.take();
					new MailUtil().sendMail(notificationMsg.subscriber1.email, "raviraghunath.ms@gmail.com", "xx", "Notifcation", notificationMsg.notificationMsg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	

}
