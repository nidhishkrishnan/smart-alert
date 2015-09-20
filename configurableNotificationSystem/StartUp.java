package configurableNotificationSystem;


public class StartUp {

	public static void main(String[] args) throws InterruptedException {
		FileProcessor1 processor = new FileProcessor1("F:\\Folder1");
		processor.doProcess();
		NotificationSystem system = NotificationSystem.getInstance();
		system.addSubscriber("Raghul","raghu@gmail.com" );
		Subscriber1 subscriber1 = DbPersist.getInstance().getSubscriber("Raghul");
		NotificationCondition condition = new NotificationCondition();
		condition.setAttributName("release date");
		condition.setProductId(13579);
		system.registerSubscriberForNotification(condition, subscriber1.getId());
		system.productDetails(13579);
	}
	}
