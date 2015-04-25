public class WorkerThread implements Runnable 
{
	int throttle;
	TransferManager tm;
	Job job;
	
	public WorkerThread(TransferManager tm, Job job, int throttle)
	{
		this.tm = tm;
		this.job = job;
		this.throttle = throttle;
	}
	
	@Override
	public void run() 
	{
		long startTime = System.nanoTime();
		long runTime = 0, sleepTime = 0;
		
		for(int i = 0; i < Global.JOB_SIZE; i++)
		{
			
			for(int j = 0; j < Global.LOOP_SIZE; j++)
			{
				runTime = System.nanoTime()-startTime;
				
				if(runTime > throttle)
				{
					//Sleep
					try {
						Thread.sleep(0, 100-throttle);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//	Amount of sleep time
					sleepTime = System.nanoTime()-startTime-runTime;
					
					//System.out.println("["+runTime+", "+sleepTime+"]");
					
					startTime = System.nanoTime();
					runTime = 0;
					startTime = 0;
				}
				job.setData(i, job.getData(i) + 1.111111);
			}
		}
		
		tm.addFinishedJob(job);
		
		System.out.println("Job " + job.getId() + " finished with value: " + job.getData(0));
	}

}
