import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.Timer;

class Client implements Communicator//CLOSE YOUR SOCKET
{
	private StateManager stateManager;
	private TransferManager transferManager;
	
	private ObjectOutputStream outToServer;
	
	public Client(String ip) throws Exception
	{
		initManagers();
		init(ip);
	}
	
	private void initManagers()
	{
		stateManager= new StateManager(this);
		transferManager= new TransferManager(this);
	}
	
	public void init(String ip) throws Exception
	{	 
		 Socket clientSocket = new Socket(ip, Global.STATE_PORT);
		 
		 outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		 ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

     	System.out.println("Waiting on server...");
		 while(true)
		 {		 
			 sendState();
			 stateManager.updateRemoteState((StateInfo)inFromServer.readObject());

			 System.out.println("Server Jobs: " + stateManager.getRemoteState().getPendingJobs());
		 }
	 }
	 
	 
	public void sendState() 
	{
		//	Put local state on state port
		try {

			StateInfo local = stateManager.getLocalState();
			if(local.getPendingJobs() > 0)
				local.setPendingJobs(local.getPendingJobs()-1);
			
			System.out.println("My jobs: " + stateManager.getLocalState().getPendingJobs());			
			
			outToServer.writeObject(stateManager.getLocalState());
			outToServer.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Override
	public void sendTransfer() {
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