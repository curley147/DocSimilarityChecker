package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadStarter {
	public void threadStart(String file1Name, String file2Name, int k, int shingleSize, int threadPoolSize) throws InterruptedException {
		BlockingQueue<Shingle> q  = new LinkedBlockingQueue<Shingle>(100);
		//first two threads for parsing two documents
		Thread t1 = new Thread(new FileParser(file1Name, shingleSize, q, 1), "T1");
		Thread t2 = new Thread(new FileParser(file2Name, shingleSize, q, 2), "T2");
		//third thread for taking shingles off queue
		Thread t3 = new Thread(new Consumer(q, k, threadPoolSize), "T3");
		//start threads
		t1.start();
		t2.start();
		t3.start();
		//join threads
		t1.join();
		t2.join();
		t3.join();
	}
}
