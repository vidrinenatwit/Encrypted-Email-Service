package encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket ss = new ServerSocket(12477);
		
		while(true) {
			Socket s = null; 
            
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                // obtaining input and out streams 
                PrintWriter pr = new PrintWriter(s.getOutputStream());
        		
        		InputStreamReader in = new InputStreamReader(s.getInputStream());
        		BufferedReader bf = new BufferedReader(in);
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                Thread t = new Thread(new ClientHandler(s, pr, bf)); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
		}
	}

}
