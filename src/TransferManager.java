import java.util.LinkedList;


public class TransferManager 
{
	//	VARIABLES
	private Communicator communicator;		//	SERVER OR CLIENT 
	
	private LinkedList<Job> jobQueue,		//	JOBS TO BE PROCESSED 
							finishedQueue;	//	JOBS THAT ARE PROCESSED
	
	
	//	CONSTRUCTOR
	
	public TransferManager(Communicator c)
	{
		communicator = c;
		jobQueue = new LinkedList<Job>();
		finishedQueue = new LinkedList<Job>();
	}
	
	
	//	JOB QUEUE
	
	public void transferJob()
	{
		if(!isEmptyJobQueue())
			communicator.sendTransfer(jobQueue.remove());
		else 
		{
			System.out.println("Requesting jobs from an empty queue.");
			sendNull();
		}
	}
	
	public void addJob(Job job)
	{
		jobQueue.add(job);
	}
	
	public Job getJob()
	{
		return jobQueue.remove();
	}
		
	public boolean isEmptyJobQueue()
	{
		return jobQueue.isEmpty();
	}
	
	public int getNumJobs()
	{
		return jobQueue.size();
	}
	
	//	FINISHED QUEUE
	
	public void transferFinishedJob()
	{
		if(!isEmptyFinishedQueue())
			communicator.sendTransfer(jobQueue.remove());
	}
	
	public void addFinishedJob(Job job)
	{
		finishedQueue.add(job);
	}
	
	public boolean isEmptyFinishedQueue()
	{
		return finishedQueue.isEmpty();
	}

	
	public void sendNull()
	{
		communicator.sendTransfer(null);
	}
	
}
