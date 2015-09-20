package configurableNotificationSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileProcessor1 {
	
	private final ConcurrentHashMap<PrdAttribute,PrdAttribute> prdAttrMap;
	private final BlockingQueue<File> fileQueue;
	private final ExecutorService fileService;
	private final ExecutorService lineService;
	private final ScheduledExecutorService service;
	private final File folder;
	private Date lastProceesedTime ;
	private final long BUFFERSIZE=8*1000;

	/** Constructor to initialize members
	 * @param folderPath
	 */
	public FileProcessor1(String folderPath){
		this.folder = new File(folderPath);
		service= Executors.newScheduledThreadPool(5);
		fileService = Executors.newFixedThreadPool(100);
		lineService = Executors.newFixedThreadPool(100);
		fileQueue = new LinkedBlockingQueue<File>();
		prdAttrMap = new ConcurrentHashMap<PrdAttribute,PrdAttribute>();
	}

	public void doProcess(){
		service.scheduleWithFixedDelay(new ProducerThread(),0, 20, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(new ConsumerThread(),10, 10, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(new PushDBThread(), 20, 20, TimeUnit.SECONDS);
	}

	class ProducerThread implements Runnable{

		@Override
		public void run() {
			getFilesFromFolder(folder);
		}
		
	}
	class ConsumerThread implements Runnable{

		@Override
		public void run() {
			while(fileQueue.size()>0){
				File file=null;
				try {
					file = fileQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(file!=null){
					System.out.println("Consumer : "+file.getName());
					fileService.execute(new ReaderThread(file));
				}
			}
		}
		
	}
	
	class PushDBThread implements Runnable{

		@Override
		public void run() {
			if(prdAttrMap.size()>=1/*00*/){
				DbPersist dbPersist = DbPersist.getInstance();
				dbPersist.insertThread(new ArrayList<FileProcessor1.PrdAttribute>(prdAttrMap.values()));
				System.out.println("Map : "+prdAttrMap.values());
				prdAttrMap.clear();
			}
		}
		
	}


	private void getFilesFromFolder(File folder){
		try{
			if(!folder.isDirectory()){return;}
			Date lastProcessedFileUpdatedTime = lastProceesedTime!=null? new Date(lastProceesedTime.getTime()):null;
			for (final File fileEntry : folder.listFiles()) {
				if (!fileEntry.isDirectory()) {
					if(fileEntry.getName().endsWith(".csv")){
						Date lastModified = new Date(fileEntry.lastModified());
						if(lastProceesedTime==null||lastModified.after(lastProceesedTime)){
							fileQueue.put(fileEntry);
							System.out.println("Producer File : "+fileEntry.getName());
							if(lastProcessedFileUpdatedTime==null||lastProcessedFileUpdatedTime.before(lastModified)){
								lastProcessedFileUpdatedTime = lastModified;
							}
						}
					}
				}
			}
			if(lastProceesedTime==null||lastProcessedFileUpdatedTime.after(lastProceesedTime)){
				lastProceesedTime =  lastProcessedFileUpdatedTime;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ReaderThread extends Thread{
		File file;
		ReaderThread(File file){
			this.file = file;
			setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
				}
			});
		}
		@Override
		public void run() {
			readFile(file);
		}
	}
	
	class ReadLineProcessorThread extends Thread{
		String line;Date lastUpdatedTime;
		ReadLineProcessorThread(String line,Date lastUpdatedTime){
			this.line = line;
			this.lastUpdatedTime = lastUpdatedTime;
		}
		@Override
		public void run() {
			System.out.println("Consumer Line : "+line);
			String[] inputs = line.split(",");
			if(inputs.length==3){
				PrdAttribute p = getPrdAttributObject(inputs,lastUpdatedTime);
				if(p!=null){
					if(prdAttrMap.containsKey(p)){
						if(p.lastUpdatedTime.after(prdAttrMap.get(p).lastUpdatedTime)){
							prdAttrMap.put(p, p);
						}
					}else{
						prdAttrMap.put(p,p);
					}
					System.out.println("Producer : "+p);
				}
			}
		}
	}
	
	private PrdAttribute getPrdAttributObject(String str[],Date date){
		try{
			return new PrdAttribute(Long.valueOf(str[0]), str[1], str[2],date);
		}catch (Exception e) {}
		return null;
	}
	
	static class PrdAttribute{
		long pId;
		String atrriName;
		String attriVal;
		Date lastUpdatedTime;
		PrdAttribute(long pId,String atrriName,String attriVal,Date lastUpdatedTime){
			this.pId=pId;
			this.atrriName=atrriName;
			this.attriVal=attriVal;
			this.lastUpdatedTime = lastUpdatedTime;
		}
		@Override
		public String toString() {
			return "PrdAttribute [pId=" + pId + ", atrriName=" + atrriName
					+ ", attriVal=" + attriVal + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((atrriName == null) ? 0 : atrriName.hashCode());
			result = prime * result + (int) (pId ^ (pId >>> 32));
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PrdAttribute other = (PrdAttribute) obj;
			if (atrriName == null) {
				if (other.atrriName != null)
					return false;
			} else if (!atrriName.equals(other.atrriName))
				return false;
			if (pId != other.pId)
				return false;
			return true;
		}
		
		
		
	}
	
	private void readFile(File file) {
		try {
			RandomAccessFile aFile = null;
			MappedByteBuffer buffer = null;
			FileChannel inChannel =null;
			aFile = new RandomAccessFile(file, "r");
			inChannel = aFile.getChannel();
			buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			buffer.load(); 	
			String subline ="";
			for (int i = 0; i < buffer.limit(); i++){
				subline += (char) buffer.get();
			}
			String []s = subline.split("[\\r\\n]+");
			for(int i =0;i<s.length;i++){
				String line = s[i];	        
				lineService.execute(new ReadLineProcessorThread(line,new Date(file.lastModified())));
			}
			buffer.clear(); 
			inChannel.close();
			aFile.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}	
		}
	
}	
