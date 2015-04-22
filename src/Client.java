import java.io.*;
import java.net.*;

class Client
{
	public Client(String ip) throws Exception
	{
		init(ip);
	}
	
	 public void init(String ip) throws Exception
	 {
		 String sentence;
		 String modifiedSentence;
		 
		 Socket clientSocket = new Socket(ip, 6789);
		 
		 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		 
		 System.out.print("Enter something: ");
		 BufferedReader inFromUser = new BufferedReader( 
				 new InputStreamReader(System.in));
		 
		 BufferedReader inFromServer = new BufferedReader(
				 new InputStreamReader(clientSocket.getInputStream()));
		 
		 sentence = inFromUser.readLine();
		 outToServer.writeBytes(sentence + '\n');
		 modifiedSentence = inFromServer.readLine();
		 System.out.println("FROM SERVER: " + modifiedSentence);
		 clientSocket.close();
	 }
}