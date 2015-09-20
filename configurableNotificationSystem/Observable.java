package configurableNotificationSystem;

public interface Observable {
	void registerSubscriber(Observer1 o);
	void removeSubscriber(Observer1 o);
	void notifySubscriber();
}
