package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class MinHasher implements Runnable{

	private BlockingQueue<Shingle> q; 
	private int[] hashes;
	private int k;
	private Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
	public MinHasher(BlockingQueue<Shingle> q, int k) {
		this.q = q;
		this.k = k;
	}
	public void getMinHashSet() {
		Random r = new Random();
		int[] hashes = new int[k];
		for(int i=0;i<hashes.length;i++) {
			hashes[i]=r.nextInt();
		}	
	} 
	public void run() {
		int numDocs = 2;
		while (numDocs>0) {
			try {
				Shingle s =q.take();
				if(s.getShingleHashCode()!=48) {
					List<Integer> list = map.get(s.getDocId());
					for(int i=0;i<hashes.length;i++) {
						int hash = s.getShingleHashCode()^hashes[i];
						if(list == null) {
							list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
							map.put(s.getDocId(), list);
						} else {
							if(list.get(i)>hash) {
								list.set(i, hash);
							}
						}
					}
					map.put(s.getDocId(), list);
				} else {
					numDocs--;
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
