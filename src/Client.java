import java.io.*;
import java.net.*;

class Client
{
	public Client()
	{
		try 
		{
			init();
		}
		catch(Exception e)
		{
			System.out.println("Client: " + e);
		}
	}
	
	 public void init() throws Exception
	 {
		 String sentence;
		 String modifiedSentence;
		 
		 System.out.print("Enter something: ");
		 BufferedReader inFromUser = new BufferedReader( 
				 new InputStreamReader(System.in));
		 
		 Socket clientSocket = new Socket("localhost", 6789);
		 
		 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		 
		 BufferedReader inFromServer = new BufferedReader(
				 new InputStreamReader(clientSocket.getInputStream()));
		 
		 sentence = inFromUser.readLine();
		 outToServer.writeBytes(sentence + '\n');
		 modifiedSentence = inFromServer.readLine();
		 System.out.println("FROM SERVER: " + modifiedSentence);
		 clientSocket.close();
	 }
}