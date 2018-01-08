package ie.gmit.sw;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserInterface {
	
	private Scanner console = new Scanner(System.in);
	
	public void start() {
		
		System.out.println("**Document Comparison Service**");
		System.out.println("Enter 1st text file name: ");
		String file1Name = console.nextLine();
		System.out.println("Enter 2nd text file name: ");
		String file2Name = console.nextLine();
		Thread t1 = new Thread(new FileParser(file1Name, 4));
		t1.start();
		Thread t2 = new Thread(new FileParser(file2Name, 4));
		t2.start();
	}
	
}
