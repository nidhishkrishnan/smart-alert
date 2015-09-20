package configurableNotificationSystem;

import configurableNotificationSystem.NotificationSystemConstants.OPERATORS;

public class NotificationCondition {

	private long productId;
	private String attributName;
	private Condition condition ;
	
	
	public static class Condition{
		OPERATORS operator;
		String value;
	}


	public long getProductId() {
		return productId;
	}


	public void setProductId(long productId) {
		this.productId = productId;
	}


	public String getAttributName() {
		return attributName;
	}


	public void setAttributName(String attributName) {
		this.attributName = attributName;
	}


	public Condition getCondition() {
		return condition;
	}


	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
}
