import java.io.Serializable;
import java.util.ArrayList;


public class StateInfo implements Serializable 
{
	
	private int pendingJobs;
	private int throttlePercentage;
	private int state;
	private int cpuUtilization;
	
	
	public StateInfo(boolean server)
	{
		if(server)
			initServerState();
		else initClientState();
	}
	
	private void initServerState()
	{
		state = Global.STATE_WAITING;
		throttlePercentage = Global.DEFAULT_THROTTLE;
		pendingJobs = 0;
		cpuUtilization = 0;
	}
	
	private void initClientState()
	{
		state = Global.STATE_WAITING;
		throttlePercentage = Global.DEFAULT_THROTTLE;
		pendingJobs = 1000;
		cpuUtilization = 0;
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
	

	public int getCpuUtilization()
	{
		return cpuUtilization;
	}
	
	public void setCpuUtilization(int cpu)
	{
		cpuUtilization = cpu;
	}
	
	
}
