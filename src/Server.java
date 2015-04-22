
import java.io.*;
import java.net.*;

class Server
{
	public Server()
	{
		try 
		{
			init();
		}
		catch(Exception e)
		{
			System.out.println("Server: " + e);
		}
	}
	
	private void init() throws Exception
    {
		String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true)
        {
           Socket connectionSocket = welcomeSocket.accept();
           BufferedReader inFromClient =
              new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
           clientSentence = inFromClient.readLine();
           System.out.println("Received: " + clientSentence);
           capitalizedSentence = clientSentence.toUpperCase() + '\n';
           outToClient.writeBytes(capitalizedSentence);
        }
     }
}
