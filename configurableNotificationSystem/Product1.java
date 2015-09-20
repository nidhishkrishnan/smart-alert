package configurableNotificationSystem;

import java.util.ArrayList;

public class Product1 {
	
	private final long productId;
	private final ArrayList<ProductData> productData;
	
	public Product1(long productId){
		this.productId = productId;
		productData = new ArrayList<ProductData>();
	}
	
	public void addProductData(ProductData data){
		this.productData.add(data);
	}
	
	
}
