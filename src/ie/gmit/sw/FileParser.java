package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
/**
 * 
 * This is a file parser class used to parse files to shingles and computing the hashcode of said shingles and adding
 * the hashcodes to a buffer
 * @author Micheal Curley
 *
 */
public class FileParser implements Runnable {
	
	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> b;
	private int docId;
	private Deque<String> buffer = new LinkedList<>();
	//Constructor
	/**
	 * 
	 * @param file - user enters file name in UserInterface
	 * @param shingleSize - how many words in each shingle
	 * @param b - queue used for shingle hashcodes
	 * @param docId - document ID entered by user in UserInterface
	 */
	public FileParser(String file, int shingleSize, BlockingQueue<Shingle> b, int docId) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.b = b;
		this.docId = docId;
	}
	/**
	 * This method creates a buffered reader to read in the text file line by line, converts the line to upper case characters and splits the line
	 * into words at all whitespaces.
	 * The words are then added to a buffer and placed into shingles and then each shingle is put on the blocking queue
	 * The buffer is then cleared for next use
	 */
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
			//clear buffer
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
	/**
	 * method to add words to buffer to be put into shingles
	 * @param words
	 */
	private void addWordsToBuffer(String[] words) {
		for (String s: words) {
			//adding shingle to buffer
			buffer.add(s);
		}
	}
	/**
	 * method to put words from buffer into shingles and if the remaining words on a line are < shingle size
	 * then the string builder adds remaining words to next line. The shingles hashcode is then computed and added to blocking queue
	 * @return returns Shingle with document ID and shingle hashcode
	 */
	private Shingle nextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			//check for more words
			if(buffer.peek()!=null) {
				//use sb to append remainder words on line to next line
				sb.append(buffer.poll());
				counter++;
			}
			else {
				//otherwise keep adding
				counter = shingleSize;
			}
		}
		if(sb.length() > 0) {
			//return shingle
			return (new Shingle(docId,sb.toString().hashCode()));
		}
		else {
			return null;
		}
	}
	/**
	 * this method checks if you are at the last word and if you are it creates a new instance of the class Poison which is used 
	 * as a marker for the end of document 
	 * @throws InterruptedException in the case of thread interruption
	 */
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
