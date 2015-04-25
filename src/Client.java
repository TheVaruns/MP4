import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.Timer;

class Client implements Communicator//CLOSE YOUR SOCKET
{
	private StateManager stateManager;
	private TransferManager transferManager;
	
	private ObjectOutputStream outToState, outToTransfer;

	
	public Client(String ip) throws Exception
	{
		initManagers();
		initJobs();
		init(ip);
	}
	
	private void initManagers()
	{
		stateManager = new StateManager(this);
		transferManager = new TransferManager(this);
		Global.hardwareMonitor = new HardwareMonitor(); 
	}
	
	private void initJobs()
	{
		for(int i = 0; i < Global.INIT_JOBS; i++)
		{
			
			double[] data = new double[Global.JOB_SIZE];
			
			for(int j = 0; j < Global.JOB_SIZE; j++)
				data[j] = 1.111111;
				
			transferManager.addJob(new Job(i, data));
		}
	}
	
	public void init(String ip) throws Exception
	{	 
		Socket stateSocket = new Socket(ip, Global.PORT_STATE);
		Socket transferSocket = new Socket(ip, Global.PORT_TRANSFER);

		outToState = new ObjectOutputStream(stateSocket.getOutputStream());
		outToTransfer = new ObjectOutputStream(transferSocket.getOutputStream());
		
		ObjectInputStream inFromState = new ObjectInputStream(stateSocket.getInputStream());
		ObjectInputStream inFromTransfer = new ObjectInputStream(transferSocket.getInputStream());
		
		int transferredJobs = 0;
		Thread thread = null;
		
		while(true)
		{		 
			int currentState = stateManager.getState();
	       	
        	switch(currentState)
        	{
        	case Global.STATE_WAITING:
        		
        		//	Listen for bootstrap state for server.
        		stateManager.updateRemoteState((StateInfo)inFromState.readObject());
        		
        		//	If server is ready for bootstrap, begin bootstrapping
        		if(stateManager.getRemoteState().getState() == Global.STATE_BOOTSTRAPPING)
        			stateManager.setState(Global.STATE_BOOTSTRAPPING);
        		
        		break;
        	case Global.STATE_BOOTSTRAPPING:
        		
        		
        		//	Send job
        		if(transferredJobs < Global.INIT_JOBS/2)
        		{
        			transferredJobs++;
        			transferManager.transferJob();
        		}
        		else
        		{
        			transferManager.sendNull();
        		}
        		
        		//	Wait for response
        		stateManager.updateRemoteState((StateInfo)inFromState.readObject());

        		System.out.println("Server State: " + stateManager.getRemoteState().getJobs());

        		//	If server is ready for bootstrap, begin bootstrapping
        		if(stateManager.getRemoteState().getState() == Global.STATE_WORKING)
        			stateManager.setState(Global.STATE_WORKING);
        		
        		break;
        	case Global.STATE_WORKING:
        		
        		//	Do new work if first job or old job hasn't finished
        		if(thread == null || !(thread.isAlive()))
        		{
        			if(!transferManager.isEmptyJobQueue())
        			{
		        		WorkerThread workerThread = 
		        				new WorkerThread(transferManager, transferManager.getJob(),
		        									Global.hardwareMonitor.getThrottle());
		        		thread = new Thread(workerThread);
		        		thread.start();
		        		
		        		//	Make sure to update number of jobs in queue.
		        		stateManager.getLocalState().setJobs(transferManager.getNumJobs());
        			}
        			else	//	REVISE ME
        			{
            			stateManager.setState(Global.STATE_AGGREGATING);
            			System.out.println("Num jobs: " + stateManager.getLocalState().getJobs());
        			}
        		}
        		
        		//	Exchange state information
        		//sendState();
        		//stateManager.updateRemoteState((StateInfo)inFromState.readObject());
        		
        		break;
        	case Global.STATE_AGGREGATING:
        		return;
        		
        		//break;
        	case Global.STATE_DONE:
        		System.out.println("Server: Job complete!");
        		return;
        		
        	default:
        		System.out.println("Server: State is invalid.");
        		return;
        	}
        	
        	//	Update state on change
        	int newState = stateManager.getState();
        	
        	if(newState != currentState)
        		System.out.println(Global.STATES[currentState] + " -> " + Global.STATES[newState]);
        	
		 }
	 }
	 
	 
	public void sendState() 
	{
		try {
			StateInfo local = stateManager.getLocalState();
			if(local.getJobs() > 0)
				local.setJobs(local.getJobs()-1);
						
			outToState.writeObject(stateManager.getLocalState());
			outToState.reset();
		} catch (IOException e) {
			System.out.println("Could not send state.");
		}

		
	}

	public void sendTransfer(Job job) 
	{
		try {
			outToTransfer.writeObject(job);
			outToTransfer.reset();
		}
		catch(Exception e){
			System.out.println("Could not send job.");
		}
	}
}