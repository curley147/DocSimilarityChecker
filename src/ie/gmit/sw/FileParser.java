package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileParser implements Runnable {

	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> b;
	
	public FileParser(String file, int shingleSize) {
		this.file = file;
		this.shingleSize = shingleSize;
	}
	public FileParser() {
	
	}
	public void run() {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine())!=null) {
				String[] words = line.split(" ");
				Shingle shingleHash = null;			
				for(int i = 0; i < words.length-(shingleSize-1); i+=shingleSize){
					shingleHash.setShingleHashCode(words[i].concat(words[i+1]).concat(words[i+2].concat(words[i+3])).hashCode());
					shingleHash.setDocId(this.file.hashCode());;
					b.add(shingleHash);
					//shingleHashes.add(shingleHash);
				}
		        
			}
			//System.out.println(shingleHashes);
			
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
