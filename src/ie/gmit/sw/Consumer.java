package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q; 
	private int[] hashes;
	private int k;
	private ExecutorService pool;
	private Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
	
	public Map<Integer, List<Integer>> getMap() {
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
				if(s.getShingleHashCode() !=48)
				{
					pool.execute(new Runnable() {
						@Override
						public void run() {
							System.out.println("second run method");
							List<Integer>list = map.get(s.getDocId());
							for(int i=0;i<hashes.length;i++) {
								System.out.println("hashes for loop");
								int value = s.getShingleHashCode() ^ hashes[i];
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
							System.out.println("out of for loop");
							map.put(s.getDocId(), list);			
						}
					});
					System.out.println("finished first run");
				}
				else {						
					docCount--;
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		System.out.println("finished second run");
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jacquared = (float)intersection.size()/(k*2-(float)intersection.size());
		
		System.out.println("J: " + jacquared);
	}
	
}
