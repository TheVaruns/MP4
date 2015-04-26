
public class HardwareMonitor 
{
	int throttle;
	StateManager sm;
	
	public HardwareMonitor(StateManager sm)
	{
		throttle = Global.DEFAULT_THROTTLE;
		this.sm = sm;
	}
	
	public int getThrottle()
	{
		return throttle;
	}
	
	public void setThrottle(int t)
	{
		throttle = t;
		sm.getLocalState().setThrottle(throttle);
	}
}
