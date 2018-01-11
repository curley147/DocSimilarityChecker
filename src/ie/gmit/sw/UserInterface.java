package ie.gmit.sw;

import java.util.Scanner;

public class UserInterface {
	//Menu
	public void start() throws InterruptedException {
		Scanner console = new Scanner(System.in);
		System.out.println("**Document Comparison Service**");
		System.out.println("Enter 1st text file name: ");
		String file1Name = console.nextLine();
		System.out.println("Enter 2nd text file name: ");
		String file2Name = console.nextLine();
		System.out.println("Enter number of min hashes: ");
		int numMinHashes= console.nextInt();
		System.out.println("Enter shingle size: ");
		int shingleSize = console.nextInt();
		System.out.println("Enter pool size: ");
		int poolSize = console.nextInt();
		//creating ThreadStarter instance
		new ThreadStarter().threadStart(file1Name, file2Name, numMinHashes, shingleSize, poolSize);
		console.close();
	}
	
}
