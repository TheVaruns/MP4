import java.io.Serializable;
import java.util.ArrayList;


public class StateInfo implements Serializable 
{
	//	STATES
	public static final int STATE_START = 0,
							DEFAULT_THROTTLE = 100;
	
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
	
	public StateInfo(StateInfo info)
	{
		this.cpuUtilization = info.cpuUtilization;
		this.pendingJobs = info.pendingJobs;
		this.state = info.state;
		this.throttlePercentage = info.throttlePercentage;
	}
	
	private void initServerState()
	{
		state = STATE_START;
		throttlePercentage = DEFAULT_THROTTLE;
		pendingJobs = 0;
		cpuUtilization = 0;
	}
	
	private void initClientState()
	{
		state = STATE_START;
		throttlePercentage = DEFAULT_THROTTLE;
		pendingJobs = 1000;
		cpuUtilization = 0;
	}
	
	public void setPendingJobs(int jobs)
	{
		pendingJobs = jobs;
	}
	
	public void setCpuUtilization(int cpu)
	{
		cpuUtilization = cpu;
	}

	public int getPendingJobs()
	{
		return pendingJobs;
	}
	
	public int getCpuUtilization()
	{
		return cpuUtilization;
	}
	
	
}
