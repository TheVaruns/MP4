public class Global 
{
	public static final int 	TIMER_DELAY = 1000,
			
								PORT_STATE = 4321,
								PORT_TRANSFER = 8765,
								
								DEFAULT_THROTTLE = 100,
								
								JOB_SIZE = 1,
								INIT_JOBS = 1000;
	

	//	STATES
	public static final int 	STATE_WAITING = 0,	 	//	SERVER: WAITING FOR CLIENT 
														//	CLIENT: WAITING ON SERVER RESPONSE
			
								STATE_BOOTSTRAPPING = 1,//	SERVER: READY FOR TRANSFERS 
														//	CLIENT: SENDING TRANSFERS
								
								STATE_WORKING = 2,		//	SERVER: PROCESSING JOBS
														//	CLIENT: PROCESSING JOBS
								
								STATE_AGGREGATING = 4,	//	SERVER: SENDING DATA
														//	CLIENT: READY FOR DATA
	
								STATE_DONE = 5;			//	SERVER: DATA SENT, JOB DONE
														//	CLIENT: DATA COLLECTED, JOB DONE.
	
	public static final String[] STATES = {"WAITING", "BOOTSTRAPPING", 
											"WORKING", "AGGREGATING", "DONE" };
								
							
}
