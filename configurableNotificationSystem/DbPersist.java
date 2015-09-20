package configurableNotificationSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import configurableNotificationSystem.FileProcessor1.PrdAttribute;

public class DbPersist {
	
	private static volatile DbPersist dbPersist;
	private static final Object lock = new Object();

	public static DbPersist getInstance(){
		DbPersist d =dbPersist;
		if(d==null){
			synchronized (lock) {
				if(d==null){
					d=new DbPersist();
					dbPersist=d;
				}
			}
		}
		return dbPersist;
	}
	
	private DbPersist(){
	}

	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
			        "jdbc:mysql://localhost:3306/test", "raghu", "raghu");
			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insertThread(final List<PrdAttribute> prdAttributes){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				insertData(prdAttributes);
			}
		});
		t.start();
	}
	
	private void insertData(List<PrdAttribute> prdAttributes){
		if(prdAttributes==null||prdAttributes.size()==0){return;}
		Set<String> attrNames = new HashSet<String>();
		Set<Long> pIds = new HashSet<Long>();
		for(PrdAttribute prdAttribute:prdAttributes){
			String str1 = prdAttribute.atrriName;
			Long pid = prdAttribute.pId;
			attrNames.add(str1);pIds.add(pid);
		}
		String attrNameString = "";
		String pIdString = "";
		for(String string : attrNames){
			attrNameString+="('"+string+"'),";
		}
		attrNameString = attrNameString.substring(0, attrNameString.length()-1);
		for(Long l : pIds){
			pIdString+="("+l+",\"\"),";
		}
		pIdString = pIdString.substring(0, pIdString.length()-1);
		String sql1 = "INSERT IGNORE INTO attribute (attribute_name) VALUES "+attrNameString;	
		String sql2 = "INSERT IGNORE INTO product VALUES "+pIdString;
		System.out.println("sql1 "+ sql1);System.out.println("sql2 "+ sql2);
		execute(sql1);execute(sql2);
		for(PrdAttribute prdAttribute:prdAttributes){
			insertPrdAttrValues(prdAttribute);
		}
	}

	
	private boolean execute(String sql){
		System.out.println("Sql : "+sql);
		try {
			Statement statement = getConnection().createStatement();
			return statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private ResultSet exeQuery(String sql){
		System.out.println("Sql : "+sql);
		try {
			Statement statement = getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);
			return set;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void insertPrdAttrValues(PrdAttribute prdAttribute){
		ExecutorService service = Executors.newFixedThreadPool(1);
		String sql1 = "SELECT last_updated_time FROM pro_atr_val WHERE  attribute_id = " +
				" (SELECT id FROM attribute WHERE attribute_name = \""+prdAttribute.atrriName+"\")AND product_id = 13579";
		ResultSet set = exeQuery(sql1);
		if(set==null){
			String nameString = "insert into pro_atr_val (product_id,attribute_id,VALUE,last_updated_time) " +
					"values ("+prdAttribute.pId+"," +
					""+"(SELECT id FROM attribute WHERE attribute_name = \""+prdAttribute.atrriName+
					"\"),\""+prdAttribute.attriVal+"\",'"+new SimpleDateFormat("yyyy-MM-dd").format(prdAttribute.lastUpdatedTime)+"')";
			execute(nameString);
			service.execute(new NotifyThread(prdAttribute));
			return;
		}
		Date date = null;
		try {
			while(set.next()){
				date = set.getDate(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(date==null||date.before(prdAttribute.lastUpdatedTime)){
			String nameString = "insert into pro_atr_val (product_id,attribute_id,VALUE,last_updated_time) " +
					"values ("+prdAttribute.pId+"," +
					""+"(SELECT id FROM attribute WHERE attribute_name = \""+prdAttribute.atrriName+
					"\"),\""+prdAttribute.attriVal+"\",'"+new SimpleDateFormat("yyyy-MM-dd").format(prdAttribute.lastUpdatedTime)+"')" +
					" ON DUPLICATE KEY UPDATE VALUE =  \""+prdAttribute.attriVal+"\"";
		execute(nameString);
		service.execute(new NotifyThread(prdAttribute));
		}
	}
	
	public ArrayList<String> getNotification(int userId){
		String s = "select a.product_id,c.attribute_name,a.value,b.value as oldVal from pro_atr_val a join user_pro_atr_val b on a.product_id = b.product_id" +
				" and a.attribute_id = b.attribute_id and a.value != b.value join attribute c on c.id = a.attribute_id" +
				" where user_id = "+userId;
		ResultSet set = exeQuery(s);
		ArrayList<String> arrayList  = new ArrayList<String>();
		if(set!=null){
			try {
				while(set.next()){
					arrayList.add("Change in "+set.getLong("product_id")+" for attribute :"+ 
							set.getString("attribute_name")+ " : Old Value is "+set.getString("oldVal")+" and new Value is "+set.getString("value"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrayList;
	}
	
	public boolean regiseterUserForNotification(int userId,NotificationCondition notificationCondition){
		String s = "INSERT IGNORE INTO user_pro_atr_val( user_id,product_id,attribute_id,VALUE) " +
				 " VALUES ("+userId+","+notificationCondition.getProductId()+","
				 + "(SELECT id FROM attribute WHERE attribute_name = \""+notificationCondition.getAttributName()+
					"\")"+",0)";
		return execute(s);
	}

	public boolean addUser(String userName,String email){
		String s = "INSERT IGNORE INTO USER (user_name,email) VALUES (\"" +userName+"\",\""+email+"\")";
		boolean t = execute(s);
		NotificationUtils.addSubscribers(userName);
		return t;
	}

	public Subscriber1 getSubscriber(String userName){
		String sql ="select id,user_name,email from user where user_name = \""+userName +"\" limit 1";
		ResultSet set = exeQuery(sql);
		Subscriber1 subscriber1=null;
		if(set!=null){
			try {
				while(set.next()){
					subscriber1 = new Subscriber1(set.getInt("id"));
					String email = set.getString("email");
					subscriber1.email=email;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return subscriber1;
	}
	
	public ArrayList<Subscriber1> getSubScribers(){
		String sql ="select id,user_name,email from user";
		ResultSet set = exeQuery(sql);
		ArrayList<Subscriber1> arrayList  = new ArrayList<Subscriber1>();
		if(set!=null){
			try {
				while(set.next()){
					Subscriber1 subscriber1 = new Subscriber1(set.getInt("id"));
					String email = set.getString("email");
					subscriber1.email=email;
					arrayList.add(subscriber1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return arrayList;
	}
	
	private CallBack getCallBackFromJsonString(String jsonString) {
		final Gson gson = new Gson();
		final CallBack dto = gson.fromJson(jsonString, CallBack.class);
		return dto;
	}

	public String getCallbackAsJsonString(CallBack callBack) {
		final Gson gson = new Gson();
		final String jsonString = gson.toJson(callBack);
		return jsonString;
	}

	public void user2BNotified(PrdAttribute prdAttribute){
		String sql = "SELECT b.id,b.user_name,b.email FROM user_pro_atr_val a JOIN USER b ON a.user_id = b.id WHERE " +
				"a.value != \""+prdAttribute.attriVal+"\" AND a.product_id = "+prdAttribute.pId+" " +
						" AND a.attribute_id = (SELECT id FROM attribute WHERE attribute_name = '"+prdAttribute.atrriName+"') ";
		ResultSet set = exeQuery(sql);
		if(set!=null){
			try {
				while(set.next()){
					Subscriber1 subscriber1 = new Subscriber1(set.getInt("id"));
					String email = set.getString("email");
					subscriber1.email= email;
					SubscriberNotificationMsg notificationMsg  = new SubscriberNotificationMsg();
					notificationMsg.subscriber1 = subscriber1;
					notificationMsg.notificationMsg = "Change in "+prdAttribute.pId+" for "+prdAttribute.atrriName+": New Value is "+prdAttribute.attriVal;
					System.out.println("chaged noted : "+notificationMsg.notificationMsg);
					NotificationUtils.addSubscribers(subscriber1);
					NotificationSystem.toBeNotifiedBlockingQueue.put(notificationMsg);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class NotifyThread implements Runnable{
		PrdAttribute prdAttribute;
		
		public NotifyThread(PrdAttribute prdAttribute) {
			this.prdAttribute = prdAttribute;
		}
		
		@Override
		public void run() {
			user2BNotified(prdAttribute);
		}
		
	}

	public ProductDetails getProductDetails(long id){
		String sql = "SELECT b.attribute_name,a.value " +
				" FROM pro_atr_val a JOIN attribute b ON a.attribute_id = b.id WHERE a.product_id = "+id;
		ResultSet set = exeQuery(sql);
		ProductDetails details = new ProductDetails();
		if(set!=null){
			try {
				while(set.next()){
					details.map.put(set.getString("attribute_name"), set.getString("value"));
				}
			}catch (Exception e) {
			}
		}
		return details;
	}

	static class ProductDetails{
		long id;
		Map<String, String> map = new HashMap<String, String>();
		@Override
		public String toString() {
			return "ProductDetails [id=" + id + ", map=" + map + "]";
		}
		
	}
	
}


