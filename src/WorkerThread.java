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
		long workTime = 0;
		
		for(int i = 0; i < Global.JOB_SIZE; i++)
		{
			for(int j = 0; j < Global.LOOP_SIZE; j++)
			{
				job.setData(i, job.getData(i) + 1.111111);
			}
		}
		
		workTime = System.nanoTime() - startTime;
		int workTimeMs = (int) (workTime/1000000);
		int workTimeNs = (int) (workTime%1000000);
		
		double factor = (100 / (double)throttle) - 1;
		
		int sleepTimeMs = (int) (workTimeMs*factor);
		int sleepTimeNs = (int) (workTimeMs*factor);
		
		try {
			Thread.sleep(sleepTimeMs, sleepTimeNs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		tm.addFinishedJob(job);
		
		System.out.println("Job " + job.getId() + " finished in: " + workTime);
	}

}
