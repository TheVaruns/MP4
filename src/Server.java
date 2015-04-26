
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server extends Communicator
{
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
	
	private void init() throws Exception
    {
		Socket stcSocket, ctsSocket, transferSocket;
		ObjectInputStream inFromClient, inFromTransfer;
		ServerSocket stcServerSocket = new ServerSocket(Global.PORT_STC);
		ServerSocket ctsServerSocket = new ServerSocket(Global.PORT_CTS);
		ServerSocket transferServerSocket = new ServerSocket(Global.PORT_TRANSFER);

		//	Waits until a client connects
		stcSocket = stcServerSocket.accept();
		ctsSocket = ctsServerSocket.accept();
		transferSocket = transferServerSocket.accept();

		//	Initializes stream for server output
	    outToClient = new ObjectOutputStream(stcSocket.getOutputStream());
	    	
	    //	Initializes stream for client input
		inFromClient = new ObjectInputStream(ctsSocket.getInputStream());

    	//	Initializes streams for server output
    	inFromTransfer = new ObjectInputStream(transferSocket.getInputStream());
    	outToTransfer = new ObjectOutputStream(transferSocket.getOutputStream());
        
    	Thread thread = null;
    	int time = 0;
    	
    	//	Loop indefinitely
        while(true)
        {	
        	int currentState = stateManager.getState();
        	
        	switch(currentState)
        	{
        	case Global.STATE_WAITING:
        		
        		//	Let client know server is ready to bootstrap
        		stateManager.setState(Global.STATE_BOOTSTRAPPING);
        		this.sendState(true);
        		
        		break;
        	case Global.STATE_BOOTSTRAPPING:
        		
        		//	Listen for new job
        		Job job = (Job)inFromTransfer.readObject();
        		
        		//	If client bootstrapping phase is done
        		if(job == null)
        		{
        			//	Move to working state
        			calculateJobTime();
        			stateManager.setState(Global.STATE_WORKING);
        			time = (int) System.currentTimeMillis();
        		}
        		else
        		{
        			//	Add job to queue
        			System.out.println("Job received: [" + job.getId() + ", " + job.getData(0) + "]");
        			
        			
        			transferManager.addJob(job);
	        		stateManager.getLocalState().setJobs(transferManager.getNumJobs());
	        		
	        		
        		}
        		//	Let client know job was received.
        		this.sendState(true);
        		
        		break;
        	case Global.STATE_WORKING:
        	
        		doWork(thread, inFromClient, inFromTransfer, time, true);
        		
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
        	
        	printIfStateChange(currentState);
        	
        }
     }

}
