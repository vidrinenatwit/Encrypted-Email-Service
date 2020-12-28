package encryption;

import java.net.*;
import java.util.Base64;
import java.io.*;

public class ClientHandler implements Runnable{
	
	public String unreadPath;
	public String readPath;
	
	public static String username;
	public static String password;
	
	public static UserDatabase UserData;
	public static RSA EncryptedMessage;
	
	public Socket s;
	public PrintWriter pr;
	public BufferedReader bf;
	
	public ClientHandler(Socket s, PrintWriter pr, BufferedReader bf) {
		this.s=s;
		this.pr=pr;
		this.bf=bf;
	}

	@Override
	public void run(){
		try {
			System.out.println(s.getRemoteSocketAddress().toString() + " has connected");
			
			PrintWriter pr = new PrintWriter(s.getOutputStream());
			
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			BufferedReader bf = new BufferedReader(in);
			
			while(true) {
				String newAccount = bf.readLine();
			
				username = bf.readLine();
			
				password = bf.readLine();
				
				UserData = new UserDatabase(username, password);
				
				if(newAccount.toLowerCase().equals("y")) {
					if(UserData.verifyLogin()) {
						pr.println(UserData.verifyLogin());
						pr.flush();
						break;
					}
					else {
						pr.println("Incorrect login information");
						pr.flush();
					}
				}
				else {
					if(!UserData.uniqueUsername()) {
						pr.println("Username already exists");
						pr.flush();
					}
					else {
						UserData.createNewUser();
						UserData.createKeys();
						pr.println("true");
						pr.flush();
						break;
					}
				}
			}
			
			unreadPath = "C:\\Users\\vidrinen\\git\\Encrypted-Email-Service\\RSA Encryption System\\DataBase\\Users\\" + username + "\\Messages\\Unread";
			readPath = "C:\\Users\\vidrinen\\git\\Encrypted-Email-Service\\RSA Encryption System\\DataBase\\Users\\" + username + "\\Messages\\Read";
			
			while(true) {
				File directory = new File(unreadPath);
				pr.println(directory.list().length);
				pr.flush();
				
				String action = bf.readLine();
				
				if(action.equals("send")) {
					String user = "";
					String title = "";
					String message = "";
					while(true) {
						user = bf.readLine();
						if(UserData.userExists(user)) {
							pr.println("go");
							pr.flush();
						}
						else {
							pr.println("User does not exist");
							pr.flush();
							continue;
						}
						title = bf.readLine();
						message = bf.readLine();
						break;
					}
					EncryptedMessage = new RSA(message, user);
					UserData.sendMessage(title, Base64.getEncoder().encodeToString(EncryptedMessage.encrypt()), user);
				}
				else if (action.equals("read")){
					String inbox = bf.readLine();
					if(inbox.equals("archived")) {
						directory = new File(readPath);
						pr.println(directory.list().length);
						pr.flush();
					}
					else {
						pr.println(directory.list().length);
						pr.flush();
					}
					if(directory.list().length>0) {
						String[] messages = directory.list();
						while(true) {
							for(int i = 0; i<directory.list().length; i++) {
								pr.println(messages[i]);
								pr.flush();
							}
							if(bf.readLine().equals("go")) break;
						}
						int showMessage = Integer.parseInt(bf.readLine());
						FileReader file;
						try {
							file = new FileReader(new File(directory.getAbsolutePath()+"\\"+messages[showMessage-1]));
							EncryptedMessage = new RSA(file, username);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						pr.println(EncryptedMessage.decrypt());
						pr.flush();
						
						File file2 = new File(directory.getAbsolutePath()+"\\"+messages[showMessage-1]);
						while(true) {
							String check = bf.readLine();
							if(check.equals("go")) break;
						}
						
						while(true) {
							String unreadAction = bf.readLine();
							if(unreadAction.equals("delete")) {
								UserData.deleteMessage(file2);
								break;
							}
							else if(unreadAction.equals("archive")) {
								UserData.moveMessage(username, messages[showMessage-1], "archive");
								break;
							}
							else if(unreadAction.equals("unread")) {
								UserData.moveMessage(username, messages[showMessage-1], "unread");
								break;
							}
							else if(unreadAction.equals("keep")) {
								break;
							}
						}
					}
				}
				else break;
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}	
}
