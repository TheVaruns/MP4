import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public abstract class Communicator 
{
	protected StateManager stateManager;
	protected TransferManager transferManager;

	protected ObjectOutputStream outToServer, outToClient, outToTransfer;
	
	protected void initManagers()
	{
		stateManager= new StateManager(this);
		transferManager= new TransferManager(this);
		Global.hardwareMonitor = new HardwareMonitor(stateManager); 
	}
	
	protected void sendState(boolean server) 
	{
		if(server)
		{
			try {
				outToClient.writeObject(stateManager.getLocalState());
				outToClient.reset();
				//System.out.println("Server sent state.");
			} catch (IOException e) {
				System.out.println("Could not send state.");
				System.out.println(e);
			}
		}
		else
		{
			try {
				outToServer.writeObject(stateManager.getLocalState());
				outToServer.reset();
				//System.out.println("Client sent state.");

			} catch (IOException e) {
				System.out.println("Could not send state.");
				System.out.println(e);
			}
		}
			
	}

	protected void sendTransfer(Job job) 
	{
		try {
			outToTransfer.writeObject(job);
			outToTransfer.reset();
		}
		catch(Exception e){
			System.out.println("Could not send job.");
		}
	}
	
	protected void doWork(Thread thread, ObjectInputStream inFromState,
							ObjectInputStream inFromTransfer, long time, boolean server) throws Exception
	{
		
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
			else if(stateManager.getRemoteState().getJobs() == 0)
			{
    			stateManager.setState(Global.STATE_AGGREGATING);
    			System.out.println("Time: " + (int)(System.currentTimeMillis()-time) + " ms");
			}
		}
		
		//	Exchange state information
		sendState(server);
		//System.out.println("State request.");
		stateManager.updateRemoteState((StateInfo)inFromState.readObject());
		//System.out.println("State received.");

		//	Check for job transfers

		long localTime = stateManager.getLocalState().calculateTotalTime();
		long remoteTime = stateManager.getRemoteState().calculateTotalTime();
		int localNumJobs = stateManager.getLocalState().getJobs();
		int remoteNumJobs = stateManager.getRemoteState().getJobs();
		
		//	If I should transfer jobs
		if(localTime > Global.THRESHOLD_JOBS*localNumJobs + remoteTime && localNumJobs > Global.THRESHOLD_JOBS*2)
		{
			System.out.println("Transferring " + Global.THRESHOLD_JOBS + " jobs.");
			int jobsSent = 0;
			while(jobsSent < Global.THRESHOLD_JOBS)
			{
				if(stateManager.getLocalState().getJobs() == 0) break;
				jobsSent++;
				
    			transferManager.transferJob();
        		stateManager.getLocalState().setJobs(transferManager.getNumJobs());

    			//	Wait for response
    			//System.out.println("Waiting for response.");
        		stateManager.updateRemoteState((StateInfo)inFromState.readObject());
    			//System.out.println("Response received.");

			}
			
			transferManager.sendNull();
		}
		//	If I should receive jobs
		else if(remoteTime > Global.THRESHOLD_JOBS*remoteNumJobs + localTime && remoteNumJobs > Global.THRESHOLD_JOBS*2)
		{
			System.out.println("Receiving " + Global.THRESHOLD_JOBS + " jobs.");
			boolean transferring = true;
			
			while(transferring)
			{
				//	Listen for new job
    			//System.out.println("Waiting for job.");
        		Job job1 = (Job)inFromTransfer.readObject();
        		
        		//	If client bootstrapping phase is done
        		if(job1 == null) transferring = false;
        		else
        		{          
        			//System.out.println("Job " + job1.getId() + " received.");
        			//	Add job
        			transferManager.addJob(job1);
	        		stateManager.getLocalState().setJobs(transferManager.getNumJobs());
	        		
	        		//	Let client know job was received.

        			//System.out.println("Sending state.");
	        		this.sendState(server);
        		}
			}
		}
		
	}
	 
	protected void calculateJobTime() 
	{
		double[] testJobs = new double[Global.TEST_CASES];
		long startNanos, timeElapsed;
		
		System.out.println("  CALCULATING JOB TIME...");
		
		startNanos = System.nanoTime();
		
		for(int i = 0; i < Global.TEST_CASES; i++)
		{
			for(int j = 0; j < Global.JOB_SIZE; j++)
				for(int k = 0; k < Global.LOOP_SIZE; k++)
					testJobs[i] += 1.111111;
			
			if(i == Global.IGNORE_CASES)
				startNanos = System.nanoTime();
		}
		
		timeElapsed = System.nanoTime()-startNanos;

		
		stateManager.getLocalState().setJobTime((int)(timeElapsed/Global.TEST_CASES));
		
		System.out.println("Average job time: " + stateManager.getLocalState().getJobTime());
	}
	
	protected void printIfStateChange(int currentState)
	{
    	//	Update state on change
    	int newState = stateManager.getState();
    	
    	if(newState != currentState)
    		System.out.println(Global.STATES[currentState] + " -> " + Global.STATES[newState]);
	}
}
