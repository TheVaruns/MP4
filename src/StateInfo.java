import java.io.Serializable;
import java.util.ArrayList;


public class StateInfo implements Serializable 
{

	private int pendingJobs;
	private int jobTime;
	private int throttle;
	private int state;
	private int cpuUtilization;
	
	
	public void printStateInfo(){
		System.out.println("State info:");
		System.out.println("  Pending jobs: " + pendingJobs);
		System.out.println("  Job time: " + jobTime);
		System.out.println("  Throttle: " + throttle);
		System.out.println("  State: " + Global.STATES[state]);
		System.out.println("  CPU Utilization: " + cpuUtilization + "\n");
	}
	
	public StateInfo(boolean server)
	{
		if(server)
			initServerState();
		else initClientState();
	}
	
	private void initServerState()
	{
		state = Global.STATE_WAITING;
		throttle = Global.DEFAULT_THROTTLE;
		pendingJobs = 0;
		cpuUtilization = 0;
		jobTime = 0;
	}
	
	private void initClientState()
	{
		state = Global.STATE_WAITING;
		throttle = Global.DEFAULT_THROTTLE;
		pendingJobs = 1000;
		cpuUtilization = 0;
		jobTime = 0;
	}

	
	public int getState()
	{
		return state;
	}
	
	public void setState(int state)
	{
		this.state = state;
	}

	public int getJobs()
	{
		return pendingJobs;
	}
	
	public void setJobs(int jobs)
	{
		pendingJobs = jobs;
	}
	
	public int getThrottle()
	{
		return throttle;
	}
	
	public void setThrottle(int t)
	{
		throttle = t;
	}

	public int getCpuUtilization()
	{
		return cpuUtilization;
	}
	
	public void setCpuUtilization(int cpu)
	{
		cpuUtilization = cpu;
	}
	
	public void setJobTime(int time)
	{
		jobTime = time;
	}
	
	public int getJobTime()
	{
		return jobTime;
	}
	
	public long calculateTotalTime()
	{
		double time = jobTime/1000000;
		return (long) ((time * pendingJobs)/throttle);
	}
	
}
