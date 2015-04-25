/*
 * 	STATE MANAGER
 * 
 */

public class StateManager 
{
	private Communicator communicator;
	private boolean server;
	private StateInfo local, remote;
	
	public StateManager(Communicator c)
	{
		communicator = c;
		server = (c instanceof Server);
		
		initLocalState();
		initRemoteState();
	}
	
	private void initLocalState()
	{
		local = new StateInfo(server);
	}
	
	private void initRemoteState()
	{
		remote = new StateInfo(!server);
	}
	
	public void updateRemoteState(StateInfo state)
	{
		remote = state;
	}
	
	
	public StateInfo getLocalState()
	{
		return local;
	}
	
	public StateInfo getRemoteState()
	{
		return remote;
	}
	

	public int getState()
	{
		return local.getState();
	}
	
	public void setState(int state)
	{
		local.setState(state);
		
		//	Whenever a state is updated, let the remote system know.
		communicator.sendState();
	}
	
	
}
