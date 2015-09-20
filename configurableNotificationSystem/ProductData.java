package configurableNotificationSystem;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductData implements Observable{

	private final long productId;
	private final String attributeName;
	private String value;
	private CopyOnWriteArrayList<Observer1> observers = new CopyOnWriteArrayList<Observer1>();
	
	public ProductData(long productId,String attributeName) {
		this.productId = productId;this.attributeName=attributeName;
	}
	
	public String getValue(){
		return this.value;
	}

	public void setValue(String value){
		this.value = value;
		valueChanged();
	}
	
	public void valueChanged(){
		notifySubscriber();
	}
	
	@Override
	public void registerSubscriber(Observer1 o) {
		observers.add(o);
	}
	@Override
	public void removeSubscriber(Observer1 o) {
		observers.remove(o);
	}
	@Override
	public void notifySubscriber() {
		for(Observer1 observer : observers){
			observer.update("Change in Value");
		}
	}  
	
}
