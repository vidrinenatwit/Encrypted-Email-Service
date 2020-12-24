package encryption;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
	
	private String message;
	private String publicKey;
	private String privateKey;
	
	private RSAUtil functions;
	
	public RSA(String message, String user) {
		this.message = message;
		
		try {
			this.publicKey = getPublicKey("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + user + "\\Keys\\" + user + "Public.key");
			this.privateKey = getPrivateKey("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + user + "\\Keys\\" + user + "Private.pub");
			functions = new RSAUtil();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public RSA(FileReader file, String user) {
		this.message = getMessage(file);
		
		try {
			this.publicKey = getPublicKey("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + user + "\\Keys\\" + user + "Public.key");
			this.privateKey = getPrivateKey("C:\\Users\\vidrinen\\eclipse-workspace\\RSA Encryption System\\DataBase\\Users\\" + user + "\\Keys\\" + user + "Private.pub");
			functions = new RSAUtil();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt() {
		try {
			return functions.encrypt(message, publicKey);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public String decrypt() {
		try {
			return functions.decrypt(message, privateKey);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			return null;
		}
	}
	
	private String getMessage(FileReader file) {
		StringBuilder fileToString = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(file);
			String line = br.readLine();
			
			while(line != null) {
				fileToString.append(line);
				line = br.readLine();
			}
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return fileToString.toString();
	}
	
	private String getPublicKey(String keyFile) throws IOException {
		Path path = Paths.get(keyFile);
		String asString = new String(Files.readAllBytes(path));
		
		return asString;
	}
	
	private String getPrivateKey(String keyFile) throws IOException {
		Path path = Paths.get(keyFile);
		String asString = new String(Files.readAllBytes(path));
		
		return asString;
	}

}
