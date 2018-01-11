package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadStarter {
	public void threadStart(String file1Name, String file2Name) throws InterruptedException {
		BlockingQueue<Shingle> q  = new LinkedBlockingQueue<Shingle>(100);
		
		Thread t1 = new Thread(new FileParser(file1Name, 4, q, 1));
		Thread t2 = new Thread(new FileParser(file2Name, 4, q, 2));
		Thread t3 = new Thread(new Consumer(q, 200, 30));
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
	}
}
