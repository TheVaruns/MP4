import java.io.Serializable;


public class StateInfo implements Serializable 
{
	//	STATES
	public static final int STATE_START;
	
	private int pendingJobs;
	private int throttlePercentage;
	private int state;
	
	
	
	public StateInfo()
	{
		
	}
}
