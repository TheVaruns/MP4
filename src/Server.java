
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

class Server implements Communicator
{
	private StateManager stateManager;
	private TransferManager transferManager;
	private String serverIp;
	
	
	public Server()
	{
		try 
		{
			getIpAddress();
			init();
		}
		catch(Exception e)
		{
			System.out.println("Server: " + e);
		}
	}
	
	private void getIpAddress()
	{
		Enumeration e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
			    NetworkInterface n = (NetworkInterface) e.nextElement();
			    Enumeration ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = (InetAddress) ee.nextElement();
			        if(i.getHostAddress().startsWith("172") || i.getHostAddress().startsWith("127"))
		        	{
			        	serverIp = i.getHostAddress();
						System.out.println("Server IP: " + serverIp);
						return;
		        	}
			    }
			}
			
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void initStateManager()
	{
		stateManager= new StateManager(this);
		transferManager= new TransferManager(this);
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

	@Override
	public void send(StateInfo info) {

	}

	@Override
	public void send(TransferInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestTransfer() {
		// TODO Auto-generated method stub
		
	}
}
