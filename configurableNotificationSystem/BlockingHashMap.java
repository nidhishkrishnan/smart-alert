package configurableNotificationSystem;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingHashMap<K,V> {


	private final HashMap<K, V> hashMap;
	private final BlockingQueue<V> blockingQueue ;
	
	public BlockingHashMap() {
		hashMap= new HashMap<K, V>();
		blockingQueue = new LinkedBlockingQueue<V>();
	}
	
	public V get(K k){
		return hashMap.get(k);
	}
	
	public void put(V v) throws InterruptedException{
		blockingQueue.put(v);
	}
	
	public V take() throws InterruptedException{
		return blockingQueue.take();
	}
}
