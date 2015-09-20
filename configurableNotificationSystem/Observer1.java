package configurableNotificationSystem;

public abstract class Observer1 {
	NotificationCondition condition;
	public abstract void setCondition(NotificationCondition condition);
	public abstract void update(String notificationMsg);
}
