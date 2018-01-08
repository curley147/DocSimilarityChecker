package ie.gmit.sw;

import java.util.Scanner;

public class UserInterface {
	
	private Scanner console = new Scanner(System.in);
	
	public void start() {
		
		System.out.println("**Document Comparison Service**");
		System.out.println("Enter 1st text file name: ");
		String file1Name = console.nextLine();
//		System.out.println("Enter 2nd text file name: ");
//		String file2Name = console.nextLine();
		FileParser fp = new FileParser(file1Name, 4);
		fp.run();
	}
	
}
