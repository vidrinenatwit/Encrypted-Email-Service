package encryption;

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class UserDatabase {
	
	private String Username;
	private String Password;
	
	private final String LOGIN_FILE_LOCATION = "C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Login Information";
	
	public UserDatabase(String Username, String Password) {
		this.Username = Username;
		this.Password = Password;
	}

	public boolean verifyLogin() {
		boolean loginExists = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(LOGIN_FILE_LOCATION));
			String userInfo = br.readLine();
			
			while(userInfo != null) {
				String[] infoFields = userInfo.split(" ");
				if(infoFields[0].equals(Username) && infoFields[1].equals(Password)) loginExists = true;
				userInfo = br.readLine();
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return loginExists;
	}

	public boolean uniqueUsername() {
		boolean isUnique = true;
		try {
			BufferedReader br = new BufferedReader(new FileReader(LOGIN_FILE_LOCATION));
			String userInfo = br.readLine();
			
			while(userInfo != null) {
				String[] infoFields = userInfo.split(" ");
				if(infoFields[0].equals(Username)) isUnique = false;
				userInfo = br.readLine();
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return isUnique;
	}
	
	public boolean userExists(String user) {
		boolean exists = false;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(LOGIN_FILE_LOCATION));
			String userInfo = br.readLine();
			
			while(userInfo != null) {
				String[] infoFields = userInfo.split(" ");
				if(infoFields[0].equals(user)) exists = true;
				userInfo = br.readLine();
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return exists;
	}

	public void createNewUser() {
		try{
			 BufferedWriter out = new BufferedWriter(new FileWriter(LOGIN_FILE_LOCATION, true));
			 out.write(Username + " " + Password + "\n");
		     out.close();
		  }catch (Exception e){
			 System.err.println("Error while writing to file: " +
		     e.getMessage());
		  }
	}
	
	void createKeys() {
		try{
			 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			 kpg.initialize(2048);
			 KeyPair kp = kpg.generateKeyPair();
			 Key PublicKey = kp.getPublic();
			 Key PrivateKey = kp.getPrivate();
			 
			 Base64.Encoder encoder = Base64.getEncoder();
			 
			 File file = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username);
			 file.mkdir();
			 
			 File file2 = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Messages");
			 file2.mkdir();
			 
			 File file4 = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Messages\\Read");
			 file4.mkdir();
			 
			 File file5 = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Messages\\Unread");
			 file5.mkdir();
			 
			 File file3 = new File("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Keys");
			 file3.mkdir();
			 
		     Writer out = new FileWriter("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Keys\\" + Username + "Public.key");
		     out.write(encoder.encodeToString(PublicKey.getEncoded()));
			 out.close();
			 
			 Writer in = new FileWriter("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + Username + "\\Keys\\" + Username + "Private.pub");
		     in.write(encoder.encodeToString(PrivateKey.getEncoded()));
			 in.close();
		  }catch (Exception e){
			 System.err.println("Error while writing to file: " +
		     e.getMessage());
		  }
	}
	
	public void sendMessage(String title, String message, String user) {
		try {
			Writer out = new FileWriter("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + user + "\\Messages\\Unread\\" + title + ".txt");
			out.write(message);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
