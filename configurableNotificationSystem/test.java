package configurableNotificationSystem;

import java.io.IOException;


public class test {

	
	public static void main(String args[]) throws IOException{
		NotificationCondition condition= new NotificationCondition();
		condition.setAttributName("price");
		condition.setProductId(13579);
		DbPersist.getInstance().regiseterUserForNotification(1, condition);
		System.out.println(DbPersist.getInstance().getNotification(1));
	}
	

}
