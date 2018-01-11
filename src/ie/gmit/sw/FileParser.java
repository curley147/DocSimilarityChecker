package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable {
	
	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> b;
	private int docId;
	private Deque<String> buffer = new LinkedList<>();
	//constuctor
	public FileParser(String file, int shingleSize, BlockingQueue<Shingle> b, int docId) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.b = b;
		this.docId = docId;
	}
	//run method
	public void run() {
		
		try {
			//parse file line by line
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine())!=null) {
				if(line.length()>0) {
					String upperCaseLine = line.toUpperCase();
					//split words by white space
					String[] words = upperCaseLine.split("\\s+");
					addWordsToBuffer(words);
					Shingle s = nextShingle();	
					b.put(s);
				}
			}
			br.close();
			flushBuffer();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void addWordsToBuffer(String[] words) {
		for (String s: words) {
			buffer.add(s);
		}
	}
	private Shingle nextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek()!=null) {
				sb.append(buffer.poll());
				counter++;
			}
			else {
				counter = shingleSize;
			}
		}
		if(sb.length() > 0) {
			return (new Shingle(docId,sb.toString().hashCode()));
		}
		else {
			return null;
		}
	}
	public void flushBuffer() throws InterruptedException{
		while(buffer.size()>0) {
			Shingle s = nextShingle();
			if(s==null) {
				b.put(s);
			}
		}
		b.put(new Poison (docId, 0));
	}
}
