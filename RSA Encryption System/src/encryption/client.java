package encryption;

import java.io.*;
import java.net.*;
import java.util.*;

public class client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner sc = new Scanner(System.in);
		Socket s = new Socket("74.80.50.203", 12477);
		
		System.out.println("You have connected");
		
		PrintWriter pr = new PrintWriter(s.getOutputStream());
		
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		
		while(true) {
			System.out.println("Do you have an account? y/n");
			String correctResponse = sc.nextLine();
			if(!correctResponse.toLowerCase().equals("y") && !correctResponse.toLowerCase().equals("n")) {
				System.out.println("Incorrect resposne");
				continue;
			}
			pr.println(correctResponse);
			pr.flush();
		
			System.out.print("Username: ");
			pr.println(sc.nextLine());
			pr.flush();
		
			System.out.print("Password: ");
			pr.println(sc.nextLine());
			pr.flush();
		
			String checker = bf.readLine();
			if(checker.equals("true")) break;
			else System.out.println(checker);
		}
		
		int unreadMessages = Integer.parseInt(bf.readLine());
		
		System.out.println("You have " + unreadMessages + " unread messages!");
		
		System.out.println("Would you like to \"send\" or \"read\" a message?");
		
		String response;
		
		while(true) {
			response = sc.nextLine();
			if(!response.toLowerCase().equals("send") && !response.toLowerCase().equals("read")) {
				System.out.println("Incorrect response");
			}
			else break;
		}
		
		pr.println(response.toLowerCase());
		pr.flush();
		
		if(response.equals("send")) {
			while(true) {
				System.out.println("Who would you like to send to?");
				pr.println(sc.nextLine());
				pr.flush();
				
				String userExists = bf.readLine();
			
				if(!userExists.equals("go")) {
					System.out.println(userExists);
					continue;
				}
			
				System.out.println("Enter your title");
				pr.println(sc.nextLine());
				pr.flush();
			
				System.out.println("Enter your message");
				pr.println(sc.nextLine());
				pr.flush();
				
				break;
			}
		}
		else {
			for(int i = 0; i<unreadMessages; i++) {
				System.out.println(bf.readLine());
			}
			System.out.println("\nPlease input the message, by number, you would like to read");
			pr.println(sc.nextLine());
			pr.flush();
			
			String message = bf.readLine();
			System.out.println(message);
		}
		
	}
}
