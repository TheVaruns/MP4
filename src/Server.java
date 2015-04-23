
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.Timer;

class Server implements Communicator
{
	
	private String serverIp;
	private Timer timer;
	private StateManager stateManager;
	private TransferManager transferManager;
	
	private ObjectOutputStream outToClient;

	private ActionListener timerListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) 
		{
			sendState();
		}
	};
	
	
	public Server()
	{
		try 
		{
			initManagers();
			initTimer();
			init();
		}
		catch(Exception e)
		{
			System.out.println("Server: " + e);
		}
	}
	
	private void initTimer() 
	{
		timer = new Timer(Global.TIMER_DELAY, timerListener );
	}
	
	private void initManagers()
	{
		stateManager= new StateManager(this);
		transferManager= new TransferManager(this);
	}
	
	private void init() throws Exception
    {
		Socket connectionSocket;
		ObjectInputStream inFromClient;
        ServerSocket welcomeSocket = new ServerSocket(Global.STATE_PORT);

    	connectionSocket = welcomeSocket.accept();
       
    	inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
    	outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
        
        while(true)
        {	
        	stateManager.updateRemoteState((StateInfo)inFromClient.readObject());
           
        	int remoteJobs = stateManager.getRemoteState().getPendingJobs();
        	System.out.println("Client Jobs: " + remoteJobs);
        	stateManager.getLocalState().setPendingJobs(1000-remoteJobs);

        	System.out.println("My Jobs: " + stateManager.getLocalState().getPendingJobs());
           
        	if(!timer.isRunning()) timer.start();
        }
     }

	public void sendState() 
	{
		//	Put local state on state port
		try {
			outToClient.writeObject(stateManager.getLocalState());
			outToClient.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void sendTransfer() 
	{
		// TODO Auto-generated method stub
		
	}

	public void requestState() {
		// TODO Auto-generated method stub
		
	}

	public void requestTransfer() {
		// TODO Auto-generated method stub
		
	}
}
