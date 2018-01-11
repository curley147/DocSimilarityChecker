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
/**
 * This is the consumer class that takes shingle hashcodes off the blocking queue and computes k number of minimum values. These 
 * values are then XORed with k number of random numbers to generate new hashCode values for the two documents which are then 
 * compared on the map and the jaccard is computed
 * @author Micheal Curley
 *
 */
public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q; 
	private int[] hashes;
	private int k;
	private ExecutorService pool;
	private ConcurrentMap<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	/**
	 * The Consumer constructor is used in ThreadStarter to instantiate a Consumer object to run on Thread "T-3"
	 * @param q - blocking queue used to take shingles off
	 * @param k - number of min hashes (entered by user in UserInterface)
	 * @param poolSize - size of thread pool (entered by user in UserInterface)
	 */
	public Consumer(BlockingQueue<Shingle> q, int k,int poolSize) {
		this.q = q;
		this.k = k;
		//initiating thread pool size
		pool = Executors.newFixedThreadPool(poolSize);
		getMinHashSet();
	}
	/**
	 * This method is used to create array of random numbers the same size as k and the minimum shingle hashcodes
	 * are XORed with these random numbers to give new hashcode values for both documents
	 */
	
	public void getMinHashSet() {
		Random r = new Random();
		hashes = new int[k];
		for(int i=0;i<hashes.length;i++) {
			hashes[i]=r.nextInt();
		}
	} 
	/**
	 * The first run method is the Consumer thread started in THreadStarter and it takes shingle of the blocking queue
	 * If its not the end of the document the second run method is invoked
	 */
	public void run() {
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
						/**
						 * The second run method is where all the work happens
						 * All the shingle hashcodes are XORed with the random numbers generated from MinHashSet
						 * and the minimum values are stored in a list.
						 * These lists are then put on the map where the document id is the key and the list of new hashcodes is the value
						 */
						@Override
						public void run() {
							List<Integer>list = map.get(s.getDocId());
							for(int i=0;i<hashes.length;i++) {
								int value = s.getShingleHashCode() ^ hashes[i];
								list = map.get(s.getDocId());
								if(list == null) {
									//if list empty create list of size k
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
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		//finished with thread pool
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//comparing new hash values that are stored in the map
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jaccard = (float)intersection.size()/(k*2-(float)intersection.size());
		float percentageReturned = Math.round(jaccard)*100;
		
		System.out.println("The documents are " + percentageReturned + "% similar.");
	}
	
}
