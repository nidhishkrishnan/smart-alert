package configurableNotificationSystem;

import java.util.ArrayList;


public class Subscriber {

	private final int id;
	private final ArrayList<NotificationCondition> conditions;
	private final ArrayList<String> notifications;
	
	
	Subscriber(int id){
		this.id=id; 
		conditions = new ArrayList<NotificationCondition>();
		notifications = new ArrayList<String>();
	}
	
	public int getSubscriberId() {
		return id;
	}

	public int getId() {
		return id;
	}

	public ArrayList<NotificationCondition> getConditions() {
		return conditions;
	}

	public ArrayList<String> getNotifications() {
		return notifications;
	}
	
	
	
}
