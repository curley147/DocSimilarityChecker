package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q; 
	private int[] hashes;
	private int k;
	private ExecutorService pool;
	private ConcurrentMap<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	
	public ConcurrentMap<Integer, List<Integer>> getMap() {
		return map;
	}
	
	public Consumer(BlockingQueue<Shingle> q, int k,int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		getMinHashSet();
	}
	public void getMinHashSet() {
		Random r = new Random();
		hashes = new int[k];
		for(int i=0;i<hashes.length;i++) {
			hashes[i]=r.nextInt();
		}
	} 
	public void run() {
		System.out.println("first run method");
		int docCount = 2;
		while(docCount > 0) {
			try {
				Shingle s = q.take();
				// when s.getHashCode returns a poison indicating EOF
				if(s instanceof Poison){
					docCount--;
				}
				else {						
					pool.execute(new Runnable() {
						@Override
						public void run() {
							System.out.println("second run method");
							List<Integer>list = map.get(s.getDocId());
							for(int i=0;i<hashes.length;i++) {
								int value = s.getShingleHashCode() ^ hashes[i];
								list = map.get(s.getDocId());
								if(list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocId(),list);
								}
								else {		
									if(list.get(i)>value) {
										list.set(i, value);
									}
								}
							} 
							map.put(s.getDocId(), list);		
						}
					});
					System.out.println("finished second run");
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jaccard = (float)intersection.size()/(k*2-(float)intersection.size());
		float percentageReturned = Math.round(jaccard)*100;
		
		System.out.println("The documents are " + percentageReturned + "% similar.");
	}
	
}
