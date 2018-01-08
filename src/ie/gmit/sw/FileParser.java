package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileParser implements Runnable {

	private File f;
	private int shingleSize;
	
	public FileParser(String file, int shingleSize) {
		this.f = new File(file);
		this.shingleSize = shingleSize;
	}
	
	public void run() {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = null;
			ArrayList<String> shingles = new ArrayList<String>();
			while((line = br.readLine())!=null) {
				String[] words = line.split(" ");
				
				for(int i = 0; i < words.length-(shingleSize-1); i+=shingleSize){
					String sh =  words[i].concat(words[i+1]).concat(words[i+2].concat(words[i+3]));
					shingles.add(sh);
				}
		        
			}
			System.out.println(shingles);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
