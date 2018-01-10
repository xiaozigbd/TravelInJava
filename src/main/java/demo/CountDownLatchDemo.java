package demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 同步辅助类
 */
public class CountDownLatchDemo {
	final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws InterruptedException {
		//两个工人的协作
    	CountDownLatch latch=new CountDownLatch(2);
    	Worker worker1=new Worker("zhang san", 5000, latch);
    	Worker worker2=new Worker("li si", 8000, latch);
    	worker1.start();//
    	worker2.start();//
    	latch.await();//等待所有工人完成工作
        System.out.println("all work done at " + sdf.format(new Date()));
	}

	public static void threadPool(){
//		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//		Worker worker1=new Worker("zhang san", 5000, latch);

	}


	static class DownloadWork extends Thread{
//		InputStream is;
//		String fileName;
//		String path;
//		public DownloadWork(InputStream is, String fileName, String path){
//			this.is=fileName;
//			this.workTime=workTime;
//			this.latch=latch;
//		}
//
//		@Override
//		public void run(){
//			System.out.println("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
//			doWork();//工作了
//			System.out.println("Worker "+workerName+" do work complete at "+sdf.format(new Date()));
//			latch.countDown();//工人完成工作，计数器减一
//
//		}
//
//		private void doWork(){
//			try {
//				Thread.sleep(workTime);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

    
    
    static class Worker extends Thread{
    	String workerName; 
    	int workTime;
    	CountDownLatch latch;
    	public Worker(String workerName ,int workTime ,CountDownLatch latch){
    		 this.workerName=workerName;
    		 this.workTime=workTime;
    		 this.latch=latch;
    	}

    	@Override
		public void run(){
    		System.out.println("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
    		doWork();//工作了
    		System.out.println("Worker "+workerName+" do work complete at "+sdf.format(new Date()));
    		latch.countDown();//工人完成工作，计数器减一

    	}
    	
    	private void doWork(){
    		try {
				Thread.sleep(workTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
     
}
