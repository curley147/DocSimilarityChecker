package ie.gmit.sw;

import java.util.Scanner;

public class UserInterface {
	
	public void start() throws InterruptedException {
		Scanner console = new Scanner(System.in);
		System.out.println("**Document Comparison Service**");
		System.out.println("Enter 1st text file name: ");
		String file1Name = console.nextLine();
		System.out.println("Enter 2nd text file name: ");
		String file2Name = console.nextLine();
		new ThreadStarter().threadStart(file1Name, file2Name);
		console.close();
	}
	
}
