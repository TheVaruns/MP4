
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
	private StateManager stateManager;
	private TransferManager transferManager;
	private HardwareMonitor hardwareMonitor;

	private ObjectOutputStream outToState, outToTransfer;

	
	
	public Server()
	{
		try 
		{
			initManagers();
			init();
		}
		catch(Exception e)
		{
			System.out.println("Server: " + e);
		}
	}

	
	private void initManagers()
	{
		stateManager= new StateManager(this);
		transferManager= new TransferManager(this);
		hardwareMonitor = new HardwareMonitor(); 
	}
	
	private void init() throws Exception
    {
		Socket stateSocket, transferSocket;
		ObjectInputStream inFromState, inFromTransfer;
		ServerSocket stateServerSocket = new ServerSocket(Global.PORT_STATE);
		ServerSocket transferServerSocket = new ServerSocket(Global.PORT_TRANSFER);

		//	Waits until a client connects
		stateSocket = stateServerSocket.accept();
		transferSocket = transferServerSocket.accept();

		//	Initializes streams for client input
    	inFromState = new ObjectInputStream(stateSocket.getInputStream());
    	outToState = new ObjectOutputStream(stateSocket.getOutputStream());

    	//	Initializes streams for server output
    	inFromTransfer = new ObjectInputStream(transferSocket.getInputStream());
    	outToTransfer = new ObjectOutputStream(transferSocket.getOutputStream());
        
    	Thread thread = null;
    	
    	//	Loop indefinitely
        while(true)
        {	
        	int currentState = stateManager.getState();
        	
        	switch(currentState)
        	{
        	case Global.STATE_WAITING:
        		
        		//	Let client know server is ready to bootstrap
        		stateManager.setState(Global.STATE_BOOTSTRAPPING);
        		this.sendState();
        		
        		break;
        	case Global.STATE_BOOTSTRAPPING:
        		
        		//	Listen for new job
        		Job job = (Job)inFromTransfer.readObject();
        		
        		//	If client bootstrapping phase is done
        		if(job == null)
        		{
        			//	Move to working state
        			stateManager.setState(Global.STATE_WORKING);
        		}
        		else
        		{
        			//	Add job to queue
        			System.out.println("Job received: [" + job.getId() + ", " + job.getData(0) + "]");
        			
        			
        			transferManager.addJob(job);
	        		stateManager.getLocalState().setJobs(transferManager.getNumJobs());
	        		
	        		//	Let client know job was received.
	        		this.sendState();
        		}
        		
        		break;
        	case Global.STATE_WORKING:
        		
				//	Do new work if first job or old job hasn't finished
        		if(thread == null || !(thread.isAlive()))
        		{
        			if(!transferManager.isEmptyJobQueue())
        			{
		        		WorkerThread workerThread = 
		        				new WorkerThread(transferManager, transferManager.getJob(),
		        									hardwareMonitor.getThrottle());
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
        		
        		
        		break;
        	case Global.STATE_DONE:
        		System.out.println("Server: Job complete!");
        		return;
        		
        	default:
        		System.out.println("Server: State is invalid.");
        		return;
        	}
        	
        	int newState = stateManager.getState();
        	
        	if(newState != currentState)
        		System.out.println(Global.STATES[currentState] + " -> " + Global.STATES[newState]);
        	
        }
     }

	public void sendState() 
	{
		try {
			outToState.writeObject(stateManager.getLocalState());
			outToState.reset();
		} catch (IOException e) {
			System.out.println("Could not send state.");
		}
	}

	public void sendTransfer(Job job) 
	{
		
	}
}
