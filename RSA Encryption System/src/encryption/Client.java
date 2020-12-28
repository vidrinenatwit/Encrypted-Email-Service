package encryption;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException, ConnectException {
		Scanner sc = new Scanner(System.in);
		Socket s = new Socket("localhost", 12477);
		
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
		
		while(true) {
			int unreadMessages = Integer.parseInt(bf.readLine());
			
			System.out.println("You have " + unreadMessages + " unread messages!");
			
			System.out.println("Would you like to \"send\", \"read\", or \"exit\" a message?");
			
			String response;
			
			while(true) {
				response = sc.nextLine();
				response = response.toLowerCase();
				if(!response.equals("send") && !response.equals("read") && !response.equals("exit")) {
					System.out.println("Incorrect response");
				}
				else break;
			}
			
			pr.println(response);
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
			else if(response.equals("read")){
				System.out.println("\"Archived\" Messages or \"Unread\" Messages?");
				String inbox;
				while(true) {
					inbox = sc.nextLine();
					inbox = inbox.toLowerCase();
					if(!inbox.equals("archived") && !inbox.equals("unread")) System.out.println("Incorrect input");
					else break;
				}
				
				pr.println(inbox);
				pr.flush();
				
				int inboxMessages = Integer.parseInt(bf.readLine());
				
				if(inboxMessages==0) {
					System.out.println("You have no unread messages\n\n");
				}
				else
				{
					String number = null;
					while(true) {
						int maxNum = 1;
						for(int i = 0; i<inboxMessages; i++) {
							System.out.println((i+1) + " " + bf.readLine());
							if(i==inboxMessages-1) maxNum = (i+1);
						}
						System.out.println("\nPlease input the message, by number, you would like to read");
						number = sc.nextLine();
						if(Integer.parseInt(number)>maxNum || Integer.parseInt(number)<=0) {
							System.out.println("Incorrect response, email does not correspond with that number\n\n");
							pr.println("no");
							pr.flush();
						}
						else {
							pr.println("go");
							pr.flush();
							break;
						}
					}
					pr.println(number);
					pr.flush();
					
					String message = bf.readLine();
					System.out.println(message);
					
					while(true) {
						if(inbox.equals("archived"))
							System.out.println("Would you like to \"delete\", \"unread\", or \"keep\" the message in archived?");
						else
							System.out.println("Would you like to \"delete\", \"archive\", or \"keep\" the message in unread?");
						String fileAction = sc.nextLine();
						fileAction = fileAction.toLowerCase();
						if(!fileAction.equals("delete") && !fileAction.equals("archive") &&
								!fileAction.equals("unread") && !fileAction.equals("keep")) {
							System.out.println("Incorrect input\n\n");
							pr.println("no");
							pr.flush();
							continue;
						}
						else {
							pr.println("go");
							pr.flush();
							
							if(fileAction.equals("delete")) {
								pr.println("delete");
								pr.flush();
								break;
							}
							else if(fileAction.equals("archive")){
								if(inbox.equals("archived")) System.out.println("Incorrect input\n\n");
								else {
									pr.println("archive");
									pr.flush();
									break;
								}
							}
							else if(fileAction.equals("unread")){
								if(inbox.equals("unread")) System.out.println("Incorrect input\n\n");
								else {
									pr.println("unread");
									pr.flush();
									break;
								}
							}
							else {
								pr.println("keep");
								pr.flush();
								break;
							}
						}
					}
				}
			}
			else break;
		}
		sc.close();
		s.close();
	}
}
