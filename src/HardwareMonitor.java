
public class HardwareMonitor 
{
	int throttle;
	
	public HardwareMonitor()
	{
		throttle = Global.DEFAULT_THROTTLE;
	}
	
	public int getThrottle()
	{
		return throttle;
	}
	
	public void setThrottle(int t)
	{
		throttle = t;
	}
}
