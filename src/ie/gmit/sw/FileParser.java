package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable {

	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> b;
	private int docId;
	
	public FileParser(String file, int shingleSize, BlockingQueue<Shingle> b, int docId) {
		this.file = file;
		this.shingleSize = shingleSize;
		this.b = b;
		this.docId = docId;
	}
	public FileParser() {
	
	}
	public void run() {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine())!=null) {
				String[] words = line.split(" ");
				Shingle s = new Shingle();			
				for(int i = 0; i < words.length-(shingleSize-1); i++){
					s.setShingleHashCode(words[i].concat(words[i+1]).concat(words[i+2].concat(words[i+3])).hashCode());
					s.setDocId(this.docId);;
				}
				b.put(s);
			}
			System.out.println("files parsed");
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
