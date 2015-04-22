/*
 * 	STATE MANAGER
 * 
 * 		- Reads remote machine state.
 * 		- Stores local machine state.
 * 		- 
 */

public class StateManager 
{
	private Communicator communicator;
	private StateInfo local, remote;
	
	public StateManager(Communicator c)
	{
		communicator = c;
		initLocalState();
	}
	
	private void initLocalState()
	{
		
	}
}
