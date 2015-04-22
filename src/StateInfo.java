import java.io.Serializable;


public class StateInfo implements Serializable 
{
	//	STATES
	public static final int STATE_START = 0;
	
	private int pendingJobs;
	private int throttlePercentage;
	private int state;
	
	
	
	public StateInfo()
	{
		
	}
}
