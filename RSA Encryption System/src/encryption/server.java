package encryption;

import java.net.*;
import java.util.Base64;
import java.io.*;

public class server {
	
	public static String username;
	public static String password;
	
	public static UserDatabase UserData;
	public static RSA EncryptedMessage;
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(12477);
		Socket s = ss.accept();
		
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
		
		File directory = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + username + "\\Messages\\Unread");
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
		else {
			String[] messages = directory.list();
			for(int i = 0; i<directory.list().length; i++) {
				pr.println(messages[i]);
				pr.flush();
			}
			int showMessage = Integer.parseInt(bf.readLine());
			FileReader file;
			try {
				file = new FileReader("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\"+ username +"\\Messages\\Unread\\"+messages[showMessage-1]);
				EncryptedMessage = new RSA(file, username);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			pr.println(EncryptedMessage.decrypt());
			pr.flush();
		}
		
	}
}
