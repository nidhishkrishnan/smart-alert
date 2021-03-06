//package configurableNotificationSystem;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class FileProcessor {
//
//	private final BlockingQueue<PrdAttribute> blockingQueue;
//	private Date lastProceesedTime ;
//	private final ExecutorService fileService;
//	private final ScheduledExecutorService service;
//	private final File folder;
//	
//	
//	/** Constructor to initialize members
//	 * @param folderPath
//	 */
//	FileProcessor(String folderPath){
//		this.folder = new File(folderPath);
//		lastProceesedTime=null;
//		service= Executors.newScheduledThreadPool(5);
//		fileService = Executors.newFixedThreadPool(100);
//		blockingQueue = new LinkedBlockingQueue<FileProcessor.PrdAttribute>();
//	}
//	
//	
//	
//
//	/**
//	 * Process Starts here.. Created 2 thread, Producer and consumer.
//	 */
//	public void doProcess(){
//		if(folder.isDirectory()){
//			service.scheduleAtFixedRate(new ProducerThread(),0, 10, TimeUnit.SECONDS);
//			service.scheduleAtFixedRate(new ConsumerThread(),0, 3, TimeUnit.SECONDS);
//		}
//	}
//	
//	
//	class ConsumerThread implements Runnable{
//
//		@Override
//		public void run() {
//			while(blockingQueue.size()>0){
//				PrdAttribute prdAttribute=null;
//				try {
//					prdAttribute = blockingQueue.take();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				if(prdAttribute!=null){
//					NotificationSystem system = NotificationSystem.getInstance();
//					long productId = prdAttribute.pId;
//					String attrName = prdAttribute.atrriName;
//					String attrVal = prdAttribute.attriVal;	
//					Product product;
//					if(system.getProductMap().containsKey(productId)){			
//						product = system.getProductMap().get(productId);
//					}else{
//						product = new Product(productId);
//						system.getProductMap().put(productId, product);
//					}
//					product.putAttribute(attrName, attrVal);
//					System.out.println("Consumer : "+prdAttribute);
//					System.out.println("Consumer : "+product);
//				}
//			}
//		}
//		
//	}
//
//	/** 
//	 * Produces the data to blocking queue , reads the data from files in the given folder;
//	 */
//	class ProducerThread implements Runnable{
//
//		@Override
//		public void run() {
//			readDataFromFolder();
//		}
//		
//	}
//
//	
//	private void readDataFromFolder(){
//		FileProp fileProp = getFilesFromFolder(folder);
//		if(fileProp!=null&&fileProp.files!=null&&fileProp.files.size()>0){
//			if(lastProceesedTime==null||fileProp.lastProcessedTime.after(lastProceesedTime)){
//				lastProceesedTime =  fileProp.lastProcessedTime;
//			}
//			System.out.println("Producer : "+fileProp.files);
//			for(File str : fileProp.files){
//				fileService.execute(new ReaderThread(str));
//			}
//		}
//	}
//	
//	class ReaderThread extends Thread{
//		File file;
//		ReaderThread(File file){
//			this.file = file;
//			setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//				@Override
//				public void uncaughtException(Thread t, Throwable e) {
//				}
//			});
//		}
//		@Override
//		public void run() {
//			readFile(file);
//		}
//	}
//	
//	static class FileProp{
//		List<File> files=new ArrayList<File>();
//		Date lastProcessedTime;
//	}
//	
//	/**
//	 * Reads all files whose last modified date is greater than the given time, from the given folder 
//	 * @param folder
//	 * @param thresholdDate
//	 */
//	private FileProp getFilesFromFolder(File folder){
//		try{
//		if(!folder.isDirectory()){return new FileProp();}
//		Date lastProcessedFileUpdatedTime = lastProceesedTime!=null? new Date(lastProceesedTime.getTime()):null;
//		List<File> files = new LinkedList<File>();
//	    for (final File fileEntry : folder.listFiles()) {
//	        if (fileEntry.isDirectory()) {
//	        	FileProp fileProp = getFilesFromFolder(fileEntry);
//	        	if(fileProp!=null){
//	        		files.addAll(fileProp.files);
//	        		if(lastProcessedFileUpdatedTime == null|| fileProp.lastProcessedTime.after(lastProcessedFileUpdatedTime)){
//	        			lastProcessedFileUpdatedTime=fileProp.lastProcessedTime;
//	        		}
//	        	}
//	        } else {
//	            if(fileEntry.getName().endsWith(".csv")){
//	            	Date lastModified = new Date(fileEntry.lastModified());
//	            	if(lastProceesedTime==null||lastModified.after(lastProceesedTime)){
//	            		files.add(fileEntry);
//	            		if(lastProcessedFileUpdatedTime==null||lastProcessedFileUpdatedTime.before(lastModified)){
//	            			lastProcessedFileUpdatedTime = lastModified;
//	            		}
//	            	}
//	            }
//	        }
//	    }
//	    FileProp fileProp = new FileProp();
//	    fileProp.files.addAll(files);
//	    fileProp.lastProcessedTime=lastProcessedFileUpdatedTime; 
//	    return fileProp;
//		}catch (Exception e) {
//			System.out.println("ex ");
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	private void readFile(File file){
//		try {
//			Scanner scanner = new Scanner(file);
//			while(scanner.hasNextLine()){
//				String line = scanner.nextLine();
//				String[] inputs = line.split(",");
//				if(inputs.length==3){
//					PrdAttribute p = getPrdAttributObject(inputs);
//					blockingQueue.put(p);
//					System.out.println("Producer : "+p);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private PrdAttribute getPrdAttributObject(String str[]){
//		try{
//			return new PrdAttribute(Long.valueOf(str[0]), str[1], str[2]);
//		}catch (Exception e) {}
//		return null;
//	}
//	
//	static class PrdAttribute{
//		long pId;
//		String atrriName;
//		String attriVal;
//		PrdAttribute(long pId,String atrriName,String attriVal){
//			this.pId=pId;
//			this.atrriName=atrriName;
//			this.attriVal=attriVal;
//		}
//		@Override
//		public String toString() {
//			return "PrdAttribute [pId=" + pId + ", atrriName=" + atrriName
//					+ ", attriVal=" + attriVal + "]";
//		}
//		
//		
//		
//	}
//	
//	
//}
